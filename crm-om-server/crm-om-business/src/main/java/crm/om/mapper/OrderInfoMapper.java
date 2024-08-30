package crm.om.mapper;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Repository
public interface OrderInfoMapper {
    /**
     * 查询正常订单
     *
     * @param orderLineId 订单行号
     * @return 订单报文
     */
    Map<String, Object> qryNormalOrder(String orderLineId);

    /**
     * 查询异常订单
     *
     * @param orderLineId 订单行号
     * @return 订单报文
     */
    Map<String, Object> qryAbnormalOrder(String orderLineId);
}




