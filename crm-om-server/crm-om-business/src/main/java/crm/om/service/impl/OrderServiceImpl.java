package crm.om.service.impl;

import crm.om.mapper.OrderInfoMapper;
import crm.om.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderInfoMapper orderInfoMapper;

    @Override
    public Map<String, Object> orderInfo(String orderLineId) {
        Map<String, Object> resultMap = new HashMap<>(8);
        // 查询正常订单
        resultMap.put("type", "A");
        Map<String, Object> orderMap = orderInfoMapper.qryNormalOrder(orderLineId);
        if (orderMap == null || orderMap.isEmpty()) {
            // 无数据查询异常订单
            orderMap = orderInfoMapper.qryAbnormalOrder(orderLineId);
            // 异常类型
            resultMap.put("type", "E");
        }

        resultMap.put("bizcont_key", orderMap.get("bizcont_key"));
        resultMap.put("bizcont_value", new String((byte[]) orderMap.get("bizcont_value"), StandardCharsets.UTF_8));
        resultMap.put("create_time", orderMap.get("create_time"));
        return resultMap;
    }
}
