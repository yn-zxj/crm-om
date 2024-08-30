package crm.om.service;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@DS("#dataSource")
public interface IOrderService {
    /**
     * 订单信息
     *
     * @param dataSource  数据源
     * @param orderLineId 订单行
     * @return 数据
     */
    Map<String, Object> orderInfo(String dataSource, String orderLineId);
}
