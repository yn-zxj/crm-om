package crm.om.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.model.ConfigInfo;
import crm.om.param.config.ConfigReq;
import crm.om.param.config.SaveReq;
import crm.om.param.config.UpdateReq;
import crm.om.service.IConfigService;
import crm.om.utils.CheckHelper;
import crm.om.vo.PageVO;
import crm.om.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 业务系统接口调用封装
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@CrossOrigin
@RestController
@Tag(name = "配置参数")
@ApiSupport(order = 400)
@RequiredArgsConstructor
@RequestMapping(value = "/config", produces = "application/json;charset=UTF-8")
public class ConfigController {

    private final CheckHelper checkHelper;
    private final IConfigService configService;

    private static final String FIELD_NAME = "paramValue";

    /**
     * 获取全部参数配置信息
     *
     * @return 参数信息
     */
    @Operation(summary = "获取配置参数信息")
    @GetMapping("/all")
    @ApiOperationSupport(order = 405)
    @Parameters({
            @Parameter(name = "platform", description = "系统平台", example = "bss"),
            @Parameter(name = "env", description = "环境", example = "test"),
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10")
    })
    public Result<PageVO<ConfigReq>> fetchAll(
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String env,
            @RequestParam Integer current,
            @RequestParam Integer size) {
        LambdaQueryWrapper<ConfigInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(platform), ConfigInfo::getPlatform, platform)
                .eq(StringUtils.isNotBlank(env), ConfigInfo::getEnv, env);

        Page<ConfigInfo> infoPage = configService.page(new Page<>(current, size), wrapper);
        // 判断类型属于json格式进行类型转换
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.setFieldValueEditor((fieldName, fieldValue) -> {
            if (FIELD_NAME.equals(fieldName) && checkHelper.isValidJson(fieldValue.toString())) {
                return JSONUtil.parse(fieldValue);
            }
            return fieldValue;
        });
        // 遍历集合中每个Bean，复制其属性到另一个类型的对象中，最后返回一个新的List
        List<ConfigReq> configReqs = BeanUtil.copyToList(infoPage.getRecords(), ConfigReq.class, copyOptions);

        PageVO<ConfigReq> pageVO = PageVO.<ConfigReq>builder()
                .pages(infoPage.getPages())
                .total(infoPage.getTotal())
                .records(configReqs)
                .build();
        return Result.ok(pageVO);
    }

    /**
     * 删除配置信息
     *
     * @param id 参数记录ID
     * @return 操作反馈
     */
    @Operation(summary = "删除配置信息")
    @ApiOperationSupport(order = 420)
    @DeleteMapping("/del/{id}")
    @Parameter(name = "id", description = "参数ID", required = true, example = "1")
    public Result<Boolean> fetchById(@PathVariable("id") String id) {
        boolean flag = configService.removeById(ConfigInfo.builder().configId(id).build());
        return Result.ok(flag);
    }

    /**
     * 新增配置信息
     *
     * @param saveReq 配置核心信息
     * @return 操作反馈
     */
    @Operation(summary = "新增配置信息")
    @ApiOperationSupport(order = 410)
    @PostMapping("/save")
    public Result<Boolean> save(@Valid @RequestBody SaveReq saveReq) {
        ConfigInfo build = ConfigInfo.builder()
                .platform(saveReq.getPlatform())
                .env(saveReq.getEnv())
                .paramName(saveReq.getParamName())
                .paramKey(saveReq.getParamKey())
                .paramValue(saveReq.getParamValue())
                // 默认启用
                .status(1)
                // todo 从头部取用户信息
                .createBy("system")
                .createTime(LocalDateTime.now())
                .build();
        boolean flag = configService.save(build);
        return Result.ok(flag);
    }

    /**
     * 更新配置信息
     *
     * @param updateReq 配置核心信息
     * @return 操作反馈
     */
    @Operation(summary = "更新配置信息")
    @ApiOperationSupport(order = 415)
    @PutMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody UpdateReq updateReq) {
        LambdaUpdateWrapper<ConfigInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StringUtils.isNotBlank(updateReq.getParamKey()), ConfigInfo::getParamKey, updateReq.getParamKey())
                .set(StringUtils.isNotBlank(updateReq.getParamValue()), ConfigInfo::getParamValue,
                        updateReq.getParamValue())
                .set(updateReq.getStatus() != null, ConfigInfo::getStatus, updateReq.getStatus())
                // todo 从头部取用户信息
                .set(ConfigInfo::getUpdateBy, "system")
                .set(ConfigInfo::getUpdateTime, DateUtil.now())
                .eq(ConfigInfo::getConfigId, updateReq.getConfigId());
        boolean flag = configService.update(wrapper);
        return Result.ok(flag);
    }
}
