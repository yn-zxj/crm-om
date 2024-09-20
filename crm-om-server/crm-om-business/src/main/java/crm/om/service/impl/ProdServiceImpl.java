package crm.om.service.impl;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import crm.om.config.DynamicDataSourceConfig;
import crm.om.enums.*;
import crm.om.exception.BaseException;
import crm.om.mapper.ProdInfoMapper;
import crm.om.model.ConfigInfo;
import crm.om.service.IProdService;
import crm.om.utils.CheckHelper;
import crm.om.utils.SqlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProdServiceImpl implements IProdService {

    private final SqlUtil sqlUtil;
    private final CheckHelper checkHelper;
    private final ProdInfoMapper prodInfoMapper;

    public final static String DATABASE_SUFFIX_PROD = "proddb";
    public final static String DATABASE_SUFFIX_CRM = "crmdb";
    public final static String DATABASE_SUFFIX_BASE = "basedb";
    public final static String DATABASE_SUFFIX_MARKET = "marketdb";

    /**
     * 国际化需要排除的表
     */
    private final static List<String> I18_EXCLUDE_TABLE = List.of("pd_prcattr_dict", "pd_proddetail_dict");

    /**
     * 全局变量存放产品ID集合
     */
    private Set<String> prodIdList = new HashSet<>();

    /**
     * 全局变量存放产品编码集合
     */
    private final Set<String> baseCodeList = new HashSet<>();

    /**
     * 数据源名称 {@code String dataSourceName}
     *
     * @param configInfo 动态查询条件
     * @param prcIdList  资费ID集合
     * @return 数据结果
     * @see DynamicDataSourceConfig#addDataSource() 参考格式: String dataSourceName
     */
    @Override
    public Map<String, Object> prodConfig(ConfigInfo configInfo, LinkedHashSet<String> prcIdList) {
        String platform = convertToEnum(configInfo.getConfigKey(), Platform.class).getCode();
        String env = convertToEnum(configInfo.getConfigKey(), Env.class).getCode();

        // eg: bss.all.test
        String configKey = platform + Constant.Symbol.DOT + "all";
        List<ConfigInfo> configInfos = this.tableInfo(configKey);

        log.info("==> 产品配置读表开始");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>(16);

        // 全局变量数据清空
        prodIdList.clear();
        baseCodeList.clear();

        // 用户库、产品库配置
        for (ConfigInfo info : configInfos) {
            String database = convertToEnum(info.getConfigKey(), DataBase.class).getCode();
            // 产品库、用户库使用 prod_prcid 和 prod_id 查询数据 [判断数据库前缀]
            if (database.startsWith(DATABASE_SUFFIX_PROD) || database.startsWith(DATABASE_SUFFIX_CRM)) {
                Map<String, Object> prodMap = prodHandler(platform, env, info, prcIdList);
                if (prodMap != null) {
                    result.putAll(prodMap);
                }
            }
        }

        // 基础库、营销库配置
        for (ConfigInfo info : configInfos) {
            String database = convertToEnum(info.getConfigKey(), DataBase.class).getCode();
            if (database.startsWith(DATABASE_SUFFIX_BASE)) {
                result.putAll(baseHandler(platform, env, info, baseCodeList));
            }

            if (database.startsWith(DATABASE_SUFFIX_MARKET)) {
                result.putAll(marketHandler(platform, env, info, prcIdList));
            }
        }

        log.info("<== 产品配置读表结束, 总耗时: {} ms", System.currentTimeMillis() - startTime);
        // 清除数据源,让其恢复默认
        DynamicDataSourceContextHolder.clear();

        return result;
    }

    @Override
    public String templateToStr(ConfigInfo configInfo, LinkedHashSet<String> prcIdList) {
        Map<String, Object> result = prodConfig(configInfo, prcIdList);

        String templateName = switch (convertToEnum(configInfo.getConfigKey(), Platform.class)) {
            case Platform.BSS -> "prodConfig/bss.ftl";
            case Platform.MVNE -> "prodConfig/mvne.ftl";
            case Platform.MVNO -> "prodConfig/mvno.ftl";
            case Platform.SGP -> "prodConfig/sgp.ftl";
        };

        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates/",
                TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate(templateName);
        return template.render(result);
    }

    /**
     * 营销库配置
     *
     * @param platform  系统
     * @param env       环境
     * @param info      营销库配置信息
     * @param prcIdList 资费ID
     * @return 营销库配置
     */
    private Map<String, Object> marketHandler(String platform, String env, ConfigInfo info,
                                              LinkedHashSet<String> prcIdList) {
        StringBuilder selectSql = new StringBuilder();
        StringBuilder insertSql = new StringBuilder();

        String database = convertToEnum(info.getConfigKey(), DataBase.class).getCode();

        // 校验格式
        if (checkHelper.isValidJson(info.getConfigValue())) {
            // 查询 element_batch_no
            List<Map<String, Object>> result = queryBuilder(platform, env, database, "mk_meanscontent_info",
                    "element_par_value", prcIdList);
            Set<String> elementBatchNoList =
                    result.stream().map(out -> out.get("ELEMENT_BATCH_NO").toString()).collect(Collectors.toSet());

            if (!elementBatchNoList.isEmpty()) {
                JSONArray tableInfos = JSONUtil.parseArray(info.getConfigValue());
                for (Object tableInfo : tableInfos) {
                    JSONObject table = (JSONObject) tableInfo;
                    String columnName = table.getStr("columnName");
                    String tableName = table.getStr("tableName");

                    List<Map<String, Object>> mapList = queryBuilder(platform, env, database, tableName, columnName,
                            elementBatchNoList);
                    conditionToStr(tableName, columnName, elementBatchNoList, insertSql, selectSql, mapList);
                }
            }
        } else {
            throw new BaseException(ResultCode.DATA_ERROR_OR_DATA_NOT_EXIST);
        }

        // 值返回
        Map<String, Object> out = new HashMap<>(4);
        out.put("marketSelect", selectSql);
        out.put("marketInsert", insertSql);

        return out;
    }

    /**
     * 产品、资费相关脚本构建
     *
     * @param platform  平台
     * @param env       环境
     * @param prodInfo  需要配置的配置表信息
     * @param prcIdList 资费ID集合
     * @return 产品、资费相关脚本
     */
    public Map<String, Object> prodHandler(String platform, String env, ConfigInfo prodInfo,
                                           LinkedHashSet<String> prcIdList) {
        StringBuilder selectSql = new StringBuilder();
        StringBuilder insertSql = new StringBuilder();

        String database = convertToEnum(prodInfo.getConfigKey(), DataBase.class).getCode();
        // 校验格式
        if (checkHelper.isValidJson(prodInfo.getConfigValue())) {
            JSONArray tableInfos = JSONUtil.parseArray(prodInfo.getConfigValue());
            for (Object tableInfo : tableInfos) {
                JSONObject table = (JSONObject) tableInfo;
                String columnName = table.getStr("columnName");
                String tableName = table.getStr("tableName");

                // 第一次查询使用资费ID，取值赋值给prodIdList，之后根据字段需要进行赋值
                Set<String> columnValue = new HashSet<>();
                List<String> keys = List.of("cata_item_id", "prod_id");
                if (keys.contains(columnName) && !prodIdList.isEmpty()) {
                    columnValue.addAll(prodIdList);
                } else {
                    columnValue.addAll(prcIdList);
                }

                // 特殊处理 eg:code_value.prod_id 拆分 前者为查询字段，后者为查询值说明
                if (columnName.contains(".")) {
                    String[] split = columnName.split("\\.");
                    String column = split[1];
                    if ("prod_id".equalsIgnoreCase(column)) {
                        columnName = split[0];
                        columnValue.addAll(prodIdList);
                    } else if ("prod_prcid".equalsIgnoreCase(column)) {
                        columnName = split[0];
                        columnValue.addAll(prcIdList);
                    }
                }

                // 数据查询
                List<Map<String, Object>> result = queryBuilder(platform, env, database, tableName, columnName,
                        columnValue);

                // 取 pd_prc_dict 中的 prod_id (特殊处理)
                if ("prod_prcid".equalsIgnoreCase(columnName) && "pd_prc_dict".equalsIgnoreCase(tableName)) {
                    prodIdList = result.stream().map(out -> out.get("PROD_ID").toString()).collect(Collectors.toSet());
                }

                String columnStr =
                        columnValue.stream().map(column -> Constant.Symbol.SINGLE_QUOTE + column + Constant.Symbol.SINGLE_QUOTE)
                                .collect(Collectors.joining(Constant.Symbol.COMMA + Constant.Symbol.SPACE));
                String select = sqlUtil.select(tableName, columnName, columnStr);
                String insert = sqlUtil.insert(tableName, result);

                // 取国际化编码[排除属性表]
                if (!I18_EXCLUDE_TABLE.contains(tableName.toLowerCase())) {
                    baseCodeList.addAll(sqlUtil.regMatch(insert));
                }

                if (StringUtils.isNotBlank(insert)) {
                    selectSql.append(select).append(Constant.Symbol.NEW_LINE);
                    insertSql.append(insert).append(Constant.Symbol.NEW_LINE);
                }
            }
        } else {
            throw new BaseException(ResultCode.DATA_ERROR);
        }

        // 值返回
        Map<String, Object> result = new HashMap<>(4);
        String keyName;
        // 其第一个的字段名作为结果返回组装字段
        JSONArray tableInfos = JSONUtil.parseArray(prodInfo.getConfigValue());
        JSONObject firstItem = (JSONObject) tableInfos.getFirst();
        String columnName = firstItem.getStr("columnName");
        if (database.startsWith(DATABASE_SUFFIX_PROD)) {
            keyName = columnName.toLowerCase().contains("prod_prcid") ? "prc" : "prod";
        } else {
            keyName = columnName.toLowerCase().contains("prod_prcid") ? "crmPrc" : "crmProd";
        }

        result.put(keyName + "Select", selectSql);
        result.put(keyName + "Insert", insertSql);

        return result;
    }

    /**
     * 国际化编码查询
     *
     * @param platform     系统
     * @param env          环境
     * @param info         配置表信息
     * @param baseCodeList 国际化编码
     * @return 国际化数据
     */
    private Map<String, Object> baseHandler(String platform, String env, ConfigInfo info, Set<String> baseCodeList) {
        StringBuilder selectSql = new StringBuilder();
        StringBuilder insertSql = new StringBuilder();

        if (!baseCodeList.isEmpty()) {
            String database = convertToEnum(info.getConfigKey(), DataBase.class).getCode();
            if (checkHelper.isValidJson(info.getConfigValue())) {
                JSONArray tableInfos = JSONUtil.parseArray(info.getConfigValue());
                for (Object tableInfo : tableInfos) {
                    JSONObject table = (JSONObject) tableInfo;
                    String columnName = table.getStr("columnName");
                    String tableName = table.getStr("tableName");

                    List<Map<String, Object>> result = queryBuilder(platform, env, database, tableName, columnName,
                            baseCodeList);
                    // 对 COED 进行排序
                    result = result.stream().sorted(Comparator.comparing(map -> (String) map.get("CODE"))).toList();
                    conditionToStr(tableName, columnName, baseCodeList, insertSql, selectSql, result);
                }
            } else {
                throw new BaseException(ResultCode.DATA_ERROR);
            }
        }

        // 值返回
        Map<String, Object> result = new HashMap<>(4);
        result.put("baseSelect", selectSql);
        result.put("baseInsert", insertSql);
        return result;
    }

    /**
     * 数据查询返回
     *
     * @param platform    系统
     * @param env         环境
     * @param database    数据库
     * @param tableName   表名
     * @param columnName  条件字段
     * @param columnValue 条件值
     * @return 数据
     */
    private List<Map<String, Object>> queryBuilder(String platform, String env, String database, String tableName,
                                                   String columnName, Object columnValue) {
        // 查询条件
        Map<String, Object> map = new HashMap<>(4);
        map.put("tableName", tableName);
        map.put("columnName", columnName);
        map.put("columnValue", columnValue);

        // 手动切换数据源
        String dataSourceName = platform + Constant.Symbol.SHORT_LINE + env + Constant.Symbol.SHORT_LINE + database;
        // 获取当前数据源名称
        String peek = DynamicDataSourceContextHolder.peek();
        // 即将切换数据源与目前不一致则进行切换
        if (!dataSourceName.equals(peek)) {
            log.info("即将切换数据源至: {}", dataSourceName);
            DynamicDataSourceContextHolder.push(dataSourceName);

            // 切换后的数据源校验
            String newDataSource = DynamicDataSourceContextHolder.peek();
            log.info("切换后数据源至: {}", newDataSource);
            if (!dataSourceName.equals(newDataSource)) {
                throw new BaseException(ResultCode.DATASOURCE_ERROR);
            }
        } else {
            log.info("当前数据源: {}", peek);
        }

        long middle = System.currentTimeMillis();
        log.info("> 表名: {}, 字段名: {}", tableName, columnName);
        List<Map<String, Object>> result = prodInfoMapper.dynamicQryTable(map);
        log.info("< 表名: {}, 字段名: {}, 耗时: {} ms", tableName, columnName, System.currentTimeMillis() - middle);
        return result;
    }

    /**
     * 查询各系统产品配置需要用到的配置表
     *
     * @param configKey 参数键名
     * @return 表配置信息
     */
    public List<ConfigInfo> tableInfo(String configKey) {
        List<ConfigInfo> tableInfos = Db.lambdaQuery(ConfigInfo.class)
                .eq(ConfigInfo::getStatus, 1)
                .eq(ConfigInfo::getConfigType, ConfigType.TABLE)
                .like(ConfigInfo::getConfigKey, configKey)
                .orderByAsc(ConfigInfo::getConfigId)
                .list();
        if (tableInfos.isEmpty()) {
            throw new BaseException(ResultCode.DATA_ERROR);
        }
        return tableInfos;
    }

    /**
     * 脚本生成
     *
     * @param tableName     表名
     * @param columnName    字段名
     * @param conditionList 条件列表
     * @param insertSql     insert脚本
     * @param selectSql     select脚本
     * @param mapList       插入脚本结果集
     */
    private void conditionToStr(String tableName, String columnName, Set<String> conditionList,
                                StringBuilder insertSql, StringBuilder selectSql, List<Map<String, Object>> mapList) {
        String columnStr =
                conditionList.stream().map(column -> Constant.Symbol.SINGLE_QUOTE + column + Constant.Symbol.SINGLE_QUOTE)
                        .collect(Collectors.joining(Constant.Symbol.COMMA + Constant.Symbol.SPACE));
        String select = sqlUtil.select(tableName, columnName, columnStr);
        String insert = sqlUtil.insert(tableName, mapList);

        if (StringUtils.isNotBlank(insert)) {
            selectSql.append(select).append(Constant.Symbol.NEW_LINE);
            insertSql.append(insert).append(Constant.Symbol.NEW_LINE);
        }
    }

    /**
     * 字符串与枚举匹配返回
     *
     * @param value     字符串值
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return 枚举值
     */
    @Override
    public <T extends Enum<T>> T convertToEnum(String value, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (value.toLowerCase().contains(each.name().toLowerCase())) {
                return each;
            }
        }
        throw new BaseException(ResultCode.DATA_ERROR);
    }
}
