package crm.om.service;

import crm.om.model.ConfigInfo;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
public interface IOrderService {
    /**
     * 订单信息
     *
     * @param orderLineId 订单行
     * @return 数据
     */
    Map<String, Object> orderInfo(String orderLineId);
}
