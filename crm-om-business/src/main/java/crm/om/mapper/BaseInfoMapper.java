package crm.om.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Repository
public interface BaseInfoMapper {
    /**
     * 基础域国际化特定前缀前120条逆序数据
     *
     * @param prefixCode 特定前缀
     * @return 国际化数据
     */
    List<Map<String, Object>> qryBaseMax(String prefixCode);

    /**
     * 基础域国际化信息查询
     *
     * @param code 国际化编码
     * @return 结果
     */
    List<Map<String, Object>> qryBaseInfo(String code);
}




