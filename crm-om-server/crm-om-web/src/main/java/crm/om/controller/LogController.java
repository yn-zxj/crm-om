package crm.om.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.model.LogInfo;
import crm.om.service.ILogService;
import crm.om.vo.PageVO;
import crm.om.vo.Result;
import crm.om.vo.log.LogVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日志管理
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@CrossOrigin
@RestController
@Tag(name = "日志管理")
@ApiSupport(order = 700)
@RequiredArgsConstructor
@RequestMapping(value = "/log", produces = "application/json;charset=UTF-8")
public class LogController {

    private final ILogService logService;

    /**
     * 获取全部参数配置信息
     *
     * @return 参数信息
     */
    @Operation(summary = "查询日志")
    @GetMapping("/all")
    @ApiOperationSupport(order = 705)
    @Parameters({
            @Parameter(name = "businessType", description = "业务说明", example = "2"),
            @Parameter(name = "requestMethod", description = "请求方法", example = "GET"),
            @Parameter(name = "opType", description = "操作类型", example = "1"),
            @Parameter(name = "status", description = "执行状态", example = "1"),
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10")
    })
    public Result<PageVO<LogVo>> fetchAll(
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String requestMethod,
            @RequestParam(required = false) String opType,
            @RequestParam(required = false) String status,
            @RequestParam Integer current,
            @RequestParam Integer size) {
        LambdaQueryWrapper<LogInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(businessType), LogInfo::getBusinessType, businessType)
                .eq(StringUtils.isNotBlank(requestMethod), LogInfo::getRequestMethod, requestMethod)
                .eq(StringUtils.isNotBlank(opType), LogInfo::getOpType, opType)
                .eq(StringUtils.isNotBlank(status), LogInfo::getStatus, status)
                .orderByAsc(LogInfo::getOpTime);

        Page<LogInfo> infoPage = logService.page(new Page<>(current, size), wrapper);

        // 遍历集合中每个Bean，复制其属性到另一个类型的对象中，最后返回一个新的List
        List<LogVo> configReqs = BeanUtil.copyToList(infoPage.getRecords(), LogVo.class);

        PageVO<LogVo> pageVO = PageVO.<LogVo>builder()
                .pages(infoPage.getPages())
                .total(infoPage.getTotal())
                .records(configReqs)
                .build();
        return Result.ok(pageVO);
    }

    /**
     * 删除日志信息
     *
     * @param id 日志ID
     * @return 操作反馈
     */
    @Operation(summary = "删除日志信息")
    @ApiOperationSupport(order = 720)
    @DeleteMapping("/del/{id}")
    @Parameter(name = "id", description = "日志ID", required = true, example = "1")
    public Result<Boolean> delById(@PathVariable("id") String id) {
        boolean flag = logService.removeById(LogInfo.builder().opId(id).build());
        return Result.ok(flag);
    }
}
