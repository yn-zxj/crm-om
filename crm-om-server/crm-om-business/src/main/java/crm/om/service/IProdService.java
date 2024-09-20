package crm.om.service;

import crm.om.model.ConfigInfo;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
public interface IProdService {
    /**
     * 获取产品配置脚本信息
     *
     * @param configInfo 系统环境信息
     * @param prcIdList  资费列表
     * @return 配置脚本
     */
    Map<String, Object> prodConfig(ConfigInfo configInfo, LinkedHashSet<String> prcIdList);

    /**
     * 产品配置信息填充模版转字符串输出
     *
     * @param configInfo 环境信息
     * @param prcIdList  资费ID列表
     * @return 模版字符串
     */
    String templateToStr(ConfigInfo configInfo, LinkedHashSet<String> prcIdList);

    /**
     * 字符串与枚举匹配返回
     *
     * @param value     字符串值
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return 枚举值
     */
    <T extends Enum<T>> T convertToEnum(String value, Class<T> enumClass);
}
