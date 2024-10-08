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
import crm.om.annotation.Log;
import crm.om.enums.BusinessType;
import crm.om.model.ConfigInfo;
import crm.om.param.config.SaveParam;
import crm.om.param.config.UpdateParam;
import crm.om.service.IConfigService;
import crm.om.utils.CheckHelper;
import crm.om.vo.PageVO;
import crm.om.vo.Result;
import crm.om.vo.config.ConfigVo;
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

    private static final String FIELD_NAME = "configValue";

    /**
     * 获取全部参数配置信息
     *
     * @return 参数信息
     */
    @Operation(summary = "获取配置参数信息")
    @GetMapping("/all")
    @ApiOperationSupport(order = 405)
    @Parameters({
            @Parameter(name = "configKey", description = "参数键名", example = "bss.test.prod"),
            @Parameter(name = "configName", description = "参数名", example = "数据库配置"),
            @Parameter(name = "configType", description = "参数类型", example = "1"),
            @Parameter(name = "status", description = "状态", example = "1"),
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10")
    })
    public Result<PageVO<ConfigVo>> fetchAll(
            @RequestParam(required = false) String configKey,
            @RequestParam(required = false) String configName,
            @RequestParam(required = false) String configType,
            @RequestParam(required = false) String status,
            @RequestParam Integer current,
            @RequestParam Integer size) {
        LambdaQueryWrapper<ConfigInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(configKey), ConfigInfo::getConfigKey, configKey)
                .like(StringUtils.isNotBlank(configName), ConfigInfo::getConfigName, configName)
                .eq(StringUtils.isNotBlank(configType), ConfigInfo::getConfigType, configType)
                .eq(StringUtils.isNotBlank(status), ConfigInfo::getStatus, status);

        Page<ConfigInfo> infoPage = configService.page(new Page<>(current, size), wrapper);
        // 判断类型属于json格式进行类型转换
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.setFieldValueEditor((fieldName, fieldValue) -> {
            if (FIELD_NAME.equalsIgnoreCase(fieldName) && checkHelper.isValidJson(fieldValue.toString())) {
                return JSONUtil.parse(fieldValue);
            }
            return fieldValue;
        });
        // 遍历集合中每个Bean，复制其属性到另一个类型的对象中，最后返回一个新的List
        List<ConfigVo> configParams = BeanUtil.copyToList(infoPage.getRecords(), ConfigVo.class, copyOptions);

        PageVO<ConfigVo> pageVO = PageVO.<ConfigVo>builder()
                .pages(infoPage.getPages())
                .total(infoPage.getTotal())
                .records(configParams)
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
    @Log(title = "删除配置信息", businessType = BusinessType.DELETE)
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
     * @param saveParam 配置核心信息
     * @return 操作反馈
     */
    @Operation(summary = "新增配置信息")
    @Log(title = "新增配置信息", businessType = BusinessType.INSERT)
    @ApiOperationSupport(order = 410)
    @PostMapping("/save")
    public Result<Boolean> save(@Valid @RequestBody SaveParam saveParam) {
        ConfigInfo build = ConfigInfo.builder()
                .configName(saveParam.getConfigName())
                .configKey(saveParam.getConfigKey())
                .configValue(saveParam.getConfigValue())
                .configType(saveParam.getConfigType())
                .status("1")
                .createTime(LocalDateTime.now())
                .build();
        boolean flag = configService.save(build);
        return Result.ok(flag);
    }

    /**
     * 更新配置信息
     *
     * @param updateParam 配置核心信息
     * @return 操作反馈
     */
    @Operation(summary = "更新配置信息")
    @Log(title = "更新配置信息", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(order = 415)
    @PutMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody UpdateParam updateParam) {
        LambdaUpdateWrapper<ConfigInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StringUtils.isNotBlank(updateParam.getConfigKey()), ConfigInfo::getConfigKey, updateParam.getConfigKey())
                .set(StringUtils.isNotBlank(updateParam.getConfigValue()), ConfigInfo::getConfigValue, updateParam.getConfigValue())
                .set(StringUtils.isNotBlank(updateParam.getStatus()), ConfigInfo::getStatus, updateParam.getStatus())
                .set(StringUtils.isNotBlank(updateParam.getConfigName()), ConfigInfo::getConfigName, updateParam.getConfigName())
                .set(StringUtils.isNotBlank(updateParam.getRemark()), ConfigInfo::getRemark, updateParam.getRemark())
                .set(StringUtils.isNotBlank(updateParam.getConfigType()), ConfigInfo::getConfigType, updateParam.getConfigType())

                .set(ConfigInfo::getUpdateBy, "system")
                .set(ConfigInfo::getUpdateTime, DateUtil.now())
                .eq(ConfigInfo::getConfigId, updateParam.getConfigId());
        boolean flag = configService.update(wrapper);
        return Result.ok(flag);
    }
}
