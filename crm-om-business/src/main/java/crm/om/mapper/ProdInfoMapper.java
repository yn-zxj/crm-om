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
     * 基础域名国际化最大值列表
     *
     * @param code 国际化编码
     * @return 国际化数据
     */
    @Select("select * from bs_i18n_config where `code` like concat(?, #{code}) order by `code` desc limit 100")
    List<Map<String, Object>> baseMax(String code);

    /**
     * 资费时间规则字典
     *
     * @param offsetUnit 偏移周期
     * @return 结果
     */
    @Select("select * from pd_timerule_dict where offset_unit = #{offsetUnit} order by offset_cycle")
    List<Map<String, Object>> pdTimeRule(String offsetUnit);

    /**
     * 资费属性规则字典
     *
     * @param elementId 属性ID
     * @return 结果
     */
    @Select("select * from pd_attrval_dict where element_id = #{elementId} order by element_id")
    List<Map<String, Object>> pdAttrRule(String elementId);

    /**
     * 产品库最大值
     *
     * @param map
     * @return 结果
     */
    @MapKey("TABLE_DATA")
    List<Map<String, Object>> prodMax(Map<String, Object> map);
}




