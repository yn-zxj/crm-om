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
     * 产品基本信息
     *
     * @param map 参数
     * @return 数据
     */
    Map<String, Object> baseInfo(Map<String, Object> map);

    /**
     * 获取产品配置脚本信息
     *
     * @param configInfo 系统环境信息
     * @param prcIdList  资费列表
     * @return 配置脚本
     */
    Map<String, Object> prodConfig(ConfigInfo configInfo, LinkedHashSet<String> prcIdList);
}
