package crm.om.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.common.enums.ResultCode;
import crm.om.common.response.Result;
import crm.om.common.utils.ApiBodyBuilder;
import crm.om.dto.bss.BssReq;
import crm.om.model.system.ConfigInfo;
import crm.om.service.IConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    private final IConfigService service;
    private final ObjectMapper objectMapper;

    /**
     * 接口调用成功状态码
     */
    private static final String SUCCESS_CODE = "0";

    /**
     * 系统通用请求接口 Method-POST
     *
     * @param bssReq 业务参数
     * @return 响应结果
     */
    @PostMapping("/common")
    @Operation(summary = "通用接口")
    public Result<Object> common(@RequestBody @Valid BssReq bssReq) {
        String apiUrl = "";

        String api = bssReq.getApi();
        Map<String, Object> reqParams = objectMapper.convertValue(bssReq.getParams(), new TypeReference<>() {
        });
        String params = apiCommon.buildParamIn(bssReq.getTenantId(), bssReq.getLang(), reqParams);
        log.info("> Api 请求接口:{}, 入参:{}", api, params);

        Map<String, Object> map = new HashMap<>(4);
        map.put("env", bssReq.getEnv());
        map.put("platform", bssReq.getPlatform());
        map.put("type", bssReq.getType());
        List<ConfigInfo> configInfos = service.listByMap(map);
        // 数据库无配置
        if (configInfos.isEmpty()) {
            return Result.build(null, ResultCode.CONFIG_NOTFOUND);
        }
        for (ConfigInfo configInfo : configInfos) {
            String paramKey = configInfo.getParamKey();
            if ("api_url".equals(paramKey)) {
                apiUrl = configInfo.getParamValue().endsWith("/") ? configInfo.getParamValue() : configInfo.getParamValue() + "/";
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
     * @param bssReq 业务参数
     * @return 响应结果
     */
    @PostMapping("/nocInfo")
    @Operation(summary = "网元指令")
    public Result<Object> nocInfo(@RequestBody @Valid BssReq bssReq) {
        String apiUrl = "";

        String api = bssReq.getApi();
        Object reqParams = bssReq.getParams();

        log.info("> NocInfo Api 请求接口:{}, 入参:{}", api, reqParams);

        Map<String, Object> map = new HashMap<>(4);
        map.put("env", bssReq.getEnv());
        map.put("platform", bssReq.getPlatform());
        map.put("type", bssReq.getType());
        List<ConfigInfo> configInfos = service.listByMap(map);
        // 数据库无配置
        if (configInfos.isEmpty()) {
            return Result.build(null, ResultCode.CONFIG_NOTFOUND);
        }
        for (ConfigInfo configInfo : configInfos) {
            apiUrl = configInfo.getParamValue().endsWith("/") ? configInfo.getParamValue() : configInfo.getParamValue() + "/";
        }

        // apiUrl 值不存在
        if (StringUtils.isBlank(apiUrl)) {
            return Result.build(null, ResultCode.DATA_ERROR);
        }

        String result = HttpUtil.post(apiUrl + api, JSONUtil.toJsonStr(reqParams));
        log.info("< NocInfo Api 响应:{}", result);

        return Result.ok(result);
    }
}
