package crm.om.config;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariCpConfig;
import crm.om.enums.ConfigType;
import crm.om.enums.Constant;
import crm.om.model.ConfigInfo;
import crm.om.service.IConfigService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 动态数据源
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicDataSourceConfig {

    @Value("${custom.dynamic.enable}")
    private Boolean dynamicDataSourceEnable;

    @Value("${spring.datasource.dynamic.datasource.master.hikari.idleTimeout}")
    private Long idleTimeout;
    @Value("${spring.datasource.dynamic.datasource.master.hikari.maxLifetime}")
    private Long maxLifetime;

    private final DataSource dataSource;
    private final IConfigService configService;
    private final DefaultDataSourceCreator dataSourceCreator;

    private final static String MYSQL_PREFIX = "jdbc:mysql://";
    private final static String MYSQL_SUFFIX = "?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8" +
            "&autoReconnect=true";
    private final static String MYSQL_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    /**
     * 根据配置文件配置项开启此功能
     */
    @PostConstruct
    public void init() {
        if (dynamicDataSourceEnable) {
            addDataSource();
        }
    }

    /**
     * 项目启动添加数据源<br/>
     * {@code String dataSourceName - 连接池名称}
     *
     * @see crm.om.service.impl.ProdServiceImpl#prodConfig(ConfigInfo, LinkedHashSet) 需要与此保持一致
     */
    public void addDataSource() {
        List<ConfigInfo> dataSourceInfo = configService.lambdaQuery()
                .eq(ConfigInfo::getConfigKey, ConfigType.DATABASE)
                .eq(ConfigInfo::getStatus, 1)
                .list();
        if (!dataSourceInfo.isEmpty()) {
            log.info("> 开始加载数据源");
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
            for (ConfigInfo configInfo : dataSourceInfo) {
                // 数据源与连接池名称 eg: mvne-prod-basedb
                String dataSourceName = configInfo.getConfigKey().replace(Constant.Symbol.DOT, Constant.Symbol.SHORT_LINE);
                JSON dataBaseInfo = JSONUtil.parse(configInfo.getConfigValue());
                // 数据库名
                String database = (String) dataBaseInfo.getByPath("database");
                DataSourceProperty dataSourceProperty = new DataSourceProperty();
                String url =
                        MYSQL_PREFIX + dataBaseInfo.getByPath("url") + Constant.Symbol.SPLIT_SLASH + dataBaseInfo.getByPath("database") + MYSQL_SUFFIX;
                dataSourceProperty.setUrl(url);
                dataSourceProperty.setUsername((String) dataBaseInfo.getByPath("username"));
                dataSourceProperty.setPassword((String) dataBaseInfo.getByPath("password"));
                dataSourceProperty.setDriverClassName(MYSQL_DRIVER_NAME);

                // 获取平台与环境结束字符下标
                int envIndex = dataSourceName.indexOf(Constant.Symbol.SHORT_LINE, dataSourceName.indexOf(Constant.Symbol.SHORT_LINE) + 1);
                // 取 param_value 中的 database 库名
                dataSourceProperty.setPoolName(dataSourceName.substring(0, envIndex) + database);

                HikariCpConfig hikariCpConfig = new HikariCpConfig();
                hikariCpConfig.setMaxLifetime(maxLifetime);
                hikariCpConfig.setIdleTimeout(idleTimeout);
                dataSourceProperty.setHikari(hikariCpConfig);

                DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
                ds.addDataSource(dataSourceName, dataSource);
            }
            // 打印加载总的数据源集合(master 是默认数据源)
            Set<String> dataSourceList = ds.getDataSources().keySet();
            log.info("成功加载数据源: {}个, 分别为 {}", dataSourceList.size(), dataSourceList);
        } else {
            log.info("> 未查询到有效数据源");
        }
    }
}
