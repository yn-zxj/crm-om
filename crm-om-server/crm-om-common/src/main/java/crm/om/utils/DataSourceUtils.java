package crm.om.utils;

import crm.om.enums.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 数据源工具类
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@Component
public class DataSourceUtils {
    /**
     * 构建数据库连接基本信息
     *
     * @param platform 平台
     * @param env      环境
     * @param database 库名
     * @return 环境字符串
     */
    public String buildEnvInfo(String platform, String env, String database) {
        return platform + Constant.Symbol.SHORT_LINE + env + Constant.Symbol.SHORT_LINE + database;
    }
}
