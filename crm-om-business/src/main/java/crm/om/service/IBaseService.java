package crm.om.service;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.List;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@DS("#dataSource")
public interface IBaseService {
    /**
     * 基础域国际化信息查询
     *
     * @param dataSource 数据源
     * @param code       国际化编码
     * @return 结果
     */
    List<Map<String, Object>> baseInfo(String dataSource, String code);

    /**
     * 基础域国际化特定前缀前120条逆序数据
     *
     * @param dataSource 数据源
     * @param prefixCode 特定前缀
     * @return 国际化数据
     */
    List<Map<String, Object>> baseMaxInfo(String dataSource, String prefixCode);
}
