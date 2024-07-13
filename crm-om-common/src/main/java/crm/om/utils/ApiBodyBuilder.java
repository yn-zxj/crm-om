package crm.om.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * BSS Api 请求公共请求体拼接
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@Component
public class ApiBodyBuilder {
    /**
     * 请求公共参数报文构建
     *
     * @param tenantId 租户: {@code HK SG}
     * @param lang     语言: {@code zh_CN zh_HK en_US}
     * @param body     业务参数 一般包括{@code BUSI_INFO}和{@code OPR_INFO}
     * @return 完整报文
     */
    public String buildParamIn(String tenantId, String lang, Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>(2);
        Map<String, Object> root = new HashMap<>(4);
        Map<String, Object> header = new HashMap<>(8);
        // 随机数
        header.put("TRACE_ID", String.valueOf(RandomUtil.randomInt(100000, 900000)));
        header.put("HSF_GROUP", "daijf");
        header.put("DB_ID", StringUtils.isBlank(tenantId) ? "HK" : tenantId);
        header.put("TENANT_ID", StringUtils.isBlank(tenantId) ? "HK" : tenantId);
        header.put("LANG", StringUtils.isBlank(tenantId) ? "zh_CN" : lang);
        Map<String, Object> route = new HashMap<>(4);
        route.put("ROUTE_KEY", "");
        route.put("ROUTE_VALUE", "");
        header.put("ROUTING", route);

        result.put("ROOT", root);
        root.put("HEADER", header);
        root.put("BODY", body);

        String paramIn = JSONUtil.toJsonStr(result);
        log.debug("接口请求参数: {}", paramIn);
        return paramIn;
    }
}
