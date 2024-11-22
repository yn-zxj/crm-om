package crm.om.controller;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.annotation.Log;
import crm.om.enums.Constant;
import crm.om.enums.DataBase;
import crm.om.model.ConfigInfo;
import crm.om.param.prod.ProdParam;
import crm.om.service.IBaseService;
import crm.om.service.IProdService;
import crm.om.utils.DataSourceUtils;
import crm.om.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品配置相关接口
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@Tag(name = "产品配置")
@ApiSupport(order = 900)
@RequiredArgsConstructor
@RequestMapping(value = "/prodConfig", produces = "application/json;charset=UTF-8")
public class ProdController {

    private final IProdService prodService;
    private final IBaseService baseService;
    private final DataSourceUtils dataSourceUtils;

    @PostMapping("/configScript")
    @ApiOperationSupport(order = 905)
    @Operation(summary = "配置脚本")
    @Log(title = "查询资费配置脚本", isSaveResponseData = false)
    public Result<String> prodConfig(@RequestBody ProdParam prodParam) {
        String configKey = prodParam.getPlatform() + Constant.Symbol.SHORT_LINE + prodParam.getEnv();
        ConfigInfo info = ConfigInfo.builder()
                .configKey(configKey.toLowerCase())
                .build();

        String result = prodService.templateToStr(info, prodParam.getPrcId());
        return Result.ok(result);
    }

    /**
     * 基础域国际化查询
     *
     * @param platform 平台
     * @param env      环境
     * @param code     国际化编码
     * @return 结果
     */
    @GetMapping("/baseInfo")
    @ApiOperationSupport(order = 915)
    @Operation(summary = "国际化配置数据查询", description = "单个基础域国际化编码数据查询")
    @Parameters({
            @Parameter(name = "code", description = "国际化编码", required = true),
            @Parameter(name = "platform", description = "平台", required = true),
            @Parameter(name = "env", description = "环境", required = true)
    })
    public Result<List<Map<String, Object>>> baseInfo(@RequestParam String code, @RequestParam String platform, @RequestParam String env) {
        DynamicDataSourceContextHolder.push(dataSourceUtils.buildEnvInfo(platform, env, DataBase.BASE.getCode()));
        List<Map<String, Object>> basedInfo = baseService.baseInfo(code);
        DynamicDataSourceContextHolder.poll();
        return Result.ok(basedInfo);
    }
}
