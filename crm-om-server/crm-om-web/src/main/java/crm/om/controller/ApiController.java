package crm.om.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.annotation.Log;
import crm.om.enums.Constant;
import crm.om.enums.DataBase;
import crm.om.enums.ResultCode;
import crm.om.model.ConfigInfo;
import crm.om.param.BssParam;
import crm.om.service.IConfigService;
import crm.om.service.IOrderService;
import crm.om.utils.ApiBodyBuilder;
import crm.om.utils.DataSourceUtils;
import crm.om.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务系统接口调用封装
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@Tag(name = "业务系统")
@ApiSupport(order = 500)
@RequiredArgsConstructor
@RequestMapping(value = "/bssApi", produces = "application/json;charset=UTF-8")
public class ApiController {

    private final ApiBodyBuilder apiCommon;
    private final IConfigService configService;
    private final ObjectMapper objectMapper;
    private final IOrderService orderService;
    private final DataSourceUtils dataSourceUtils;

    /**
     * 接口调用成功状态码
     */
    private static final String SUCCESS_CODE = "0";

    /**
     * 系统通用请求接口 Method-POST
     *
     * @param bssParam 业务参数
     * @return 响应结果
     */
    @PostMapping("/common")
    @ApiOperationSupport(order = 505)
    @Operation(summary = "通用接口")
    @Log(title = "通用接口调用")
    public Result<Object> common(@RequestBody @Valid BssParam bssParam) {
        String apiUrl = "";

        String api = bssParam.getApi();
        Map<String, Object> reqParams = objectMapper.convertValue(bssParam.getParams(), new TypeReference<>() {
        });
        String params = apiCommon.buildParamIn(bssParam.getTenantId(), bssParam.getLang(), reqParams);
        log.info("> Api 请求接口:{}, 入参:{}", api, params);

        LambdaQueryWrapper<ConfigInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConfigInfo::getConfigKey, bssParam.getPlatform().getCode() + Constant.Symbol.DOT + bssParam.getEnv().getCode())
                .eq(ConfigInfo::getConfigType, String.valueOf(bssParam.getType().getCode()));
        List<ConfigInfo> configInfos = configService.list(wrapper);

        // 数据库无配置
        if (configInfos.isEmpty()) {
            return Result.build(null, ResultCode.CONFIG_NOTFOUND);
        }
        for (ConfigInfo configInfo : configInfos) {
            String paramKey = configInfo.getRemark();
            if ("api_url".equals(paramKey)) {
                apiUrl = configInfo.getConfigValue().endsWith("/") ? configInfo.getConfigValue() :
                        configInfo.getConfigValue() + "/";
            }
        }

        // apiUrl 值不存在
        if (StringUtils.isBlank(apiUrl)) {
            return Result.build(null, ResultCode.DATA_ERROR);
        }

        String result = HttpUtil.post(apiUrl + api, params);
        log.info("< Api 响应:{}", result);
        JSON parse = JSONUtil.parse(result);
        String returnCode = (String) parse.getByPath("ROOT.BODY.RETURN_CODE");
        String returnMsg = (String) parse.getByPath("ROOT.RETURN_MSG");
        if (SUCCESS_CODE.equals(returnCode)) {
            // 返回 OUT_DATA 节点数据
            return Result.ok(parse.getByPath("ROOT.BODY.OUT_DATA"));
        } else if (StringUtils.isNotEmpty(returnMsg)) {
            // 返回接口异常信息
            return Result.build(parse.getByPath("ROOT.RETURN_MSG"), ResultCode.FAIL);
        } else {
            return Result.build(parse.getByPath("ROOT.BODY"), ResultCode.FAIL);
        }
    }

    /**
     * 网元指令查询
     *
     * @param bssParam 业务参数
     * @return 响应结果
     */
    @PostMapping("/nocInfo")
    @ApiOperationSupport(order = 510)
    @Operation(summary = "网元指令")
    public Result<Object> nocInfo(@RequestBody @Valid BssParam bssParam) {
        String apiUrl = "";

        String api = bssParam.getApi();
        Object reqParams = bssParam.getParams();

        log.info("> NocInfo Api 请求接口:{}, 入参:{}", api, reqParams);

        LambdaQueryWrapper<ConfigInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConfigInfo::getConfigKey, bssParam.getPlatform().getCode() + Constant.Symbol.DOT + bssParam.getEnv().getCode())
                .eq(ConfigInfo::getConfigType, String.valueOf(bssParam.getType().getCode()));
        List<ConfigInfo> configInfos = configService.list(wrapper);

        // 数据库无配置
        if (configInfos.isEmpty()) {
            return Result.build(null, ResultCode.CONFIG_NOTFOUND);
        }
        for (ConfigInfo configInfo : configInfos) {
            apiUrl = configInfo.getConfigValue().endsWith("/") ? configInfo.getConfigValue() :
                    configInfo.getConfigValue() + "/";
        }

        // apiUrl 值不存在
        if (StringUtils.isBlank(apiUrl)) {
            return Result.build(null, ResultCode.DATA_ERROR);
        }

        String result = HttpUtil.post(apiUrl + api, JSONUtil.toJsonStr(reqParams));
        log.info("< NocInfo Api 响应:{}", result);

        return Result.ok(result);
    }

    /**
     * 订单信息
     *
     * @param orderLineId 订单行号
     * @param platform    平台
     * @param env         环境
     * @return 结果
     */
    @GetMapping("/orderInfo")
    @ApiOperationSupport(order = 515)
    @Operation(summary = "订单信息")
    @Parameters({
            @Parameter(name = "orderLineId", description = "订单行号", required = true),
            @Parameter(name = "platform", description = "平台", required = true),
            @Parameter(name = "env", description = "环境", required = true)
    })
    @Log(title = "查询订单信息", isSaveResponseData = false)
    public Result<Map<String, Object>> orderInfo(@RequestParam String orderLineId, @RequestParam String platform, @RequestParam String env) {
        DynamicDataSourceContextHolder.push(dataSourceUtils.buildEnvInfo(platform, env, DataBase.ORDER.getCode()));
        Map<String, Object> orderInfo = orderService.orderInfo(orderLineId);
        DynamicDataSourceContextHolder.poll();
        return Result.ok(orderInfo);
    }
}



