package crm.om.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Repository
public interface ProdInfoMapper {
    /**
     * 动态表信息查询(忽略sql拦截插件)
     *
     * @param map 条件(条件及条件值)
     * @return 表数据
     */
    @InterceptorIgnore(illegalSql = "true")
    @MapKey("TABLE_DATA")
    List<Map<String, Object>> dynamicQryTable(Map<String, Object> map);

    /**
     * 资费时间规则字典
     *
     * @param offsetUnit 偏移周期
     * @return 结果
     */
    List<Map<String, Object>> qryPrcTimeRule(String offsetUnit);

    /**
     * 资费属性规则字典
     *
     * @param elementId 属性ID
     * @return 结果
     */
    List<Map<String, Object>> qryPrcAttrRule(String elementId);

    /**
     * 产品库最大值
     *
     * @param map
     * @return 结果
     */
    @MapKey("TABLE_DATA")
    List<Map<String, Object>> prodMax(Map<String, Object> map);
}




