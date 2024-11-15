package crm.om.service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
public interface IBaseService {
    /**
     * 基础域国际化信息查询
     *
     * @param code 国际化编码
     * @return 结果
     */
    List<Map<String, Object>> baseInfo(String code);
}
