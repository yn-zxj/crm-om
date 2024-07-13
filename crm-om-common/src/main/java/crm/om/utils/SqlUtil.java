package crm.om.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import crm.om.exception.BaseException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * SQL 转化工具类
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Component
public class SqlUtil {

    private final static String BACKQUOTE = "`";
    private final static String COMMA = ",";
    private final static String IN = "IN";
    private final static String INSERT_INTO = "INSERT INTO";
    private final static String SELECT_ALL_FROM = "SELECT * FROM";
    private final static String WHERE = "WHERE";
    private final static String VALUES = "VALUES";
    private final static String SPACE = " ";
    private final static String SINGLE_QUOTE = "'";
    private final static String SEMICOLON = ";";
    private final static String LEFT_BRACKET = "(";
    private final static String RIGHT_BRACKET = ")";
    private final static String NEW_LINE = System.lineSeparator();

    /**
     * 查询语句拼接 <br/>
     * 自行保证条件值格式正确
     *
     * @param tableName      表
     * @param conditionKey   条件
     * @param conditionValue 条件值
     * @return 拼接后的 SQL 语句
     */
    public String select(String tableName, String conditionKey, String conditionValue) {
        return SELECT_ALL_FROM + SPACE + BACKQUOTE + tableName.toUpperCase() + BACKQUOTE + SPACE + WHERE +
                SPACE + BACKQUOTE + conditionKey.toUpperCase() + BACKQUOTE + SPACE + IN + SPACE + LEFT_BRACKET +
                conditionValue + RIGHT_BRACKET + SEMICOLON;
    }

    /**
     * 将 List<Map> 数据转换为 SQL 插入语句
     *
     * @param tableName 表名
     * @param dataList  数据
     * @return SQL 插入语句
     */
    public String insert(String tableName, List<Map<String, Object>> dataList) {
        StringBuilder sql = new StringBuilder();

        if (dataList == null || dataList.isEmpty()) {
            return "";
        }

        // 获取列名
        Map<String, Object> firstRow = dataList.getFirst();
        String[] columns = firstRow.keySet().toArray(new String[0]);

        // 构建INSERT语句
        sql.append(INSERT_INTO).append(SPACE).append(BACKQUOTE).append(tableName.toUpperCase()).append(BACKQUOTE).append(SPACE).append("(");
        for (String column : columns) {
            sql.append(BACKQUOTE).append(column.toUpperCase()).append(BACKQUOTE).append(COMMA).append(SPACE);
        }
        // 删除最后的逗号和空格
        sql.setLength(sql.length() - 2);
        sql.append(RIGHT_BRACKET).append(SPACE).append(VALUES).append(NEW_LINE);

        // 构建VALUES部分
        for (Map<String, Object> row : dataList) {
            sql.append(LEFT_BRACKET);
            for (String column : columns) {
                Object value = row.get(column);
                if (value instanceof String) {
                    sql.append(SINGLE_QUOTE).append(value).append(SINGLE_QUOTE).append(COMMA).append(SPACE);
                } else if (value instanceof LocalDateTime) {
                    sql.append(SINGLE_QUOTE).append(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append(SINGLE_QUOTE).append(COMMA).append(SPACE);
                } else {
                    sql.append(value).append(COMMA).append(SPACE);
                }
            }
            // 删除最后的逗号和空格
            sql.setLength(sql.length() - 2);
            sql.append(RIGHT_BRACKET).append(COMMA).append(NEW_LINE);
        }
        // 删除最后的逗号和换行符
        sql.setLength(sql.length() - 2);
        sql.append(SEMICOLON);

        return sql.toString();
    }

    /**
     * 将 JSON 字符串转换为 SQL 插入语句
     *
     * @param tableName  表名
     * @param jsonString JSON 字符串数据
     * @return SQL 插入语句
     */
    public String insert(String tableName, String jsonString) {
        StringBuilder sql = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 解析 JSON 字符串为 List<Map<String, Object>>
            List<Map<String, Object>> list = objectMapper.readValue(jsonString, new TypeReference<>() {
            });
            sql.append(this.insert(tableName, list));
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
        return sql.toString();
    }

    /**
     * 提取脚本中的国际化编码CODE
     *
     * @param str insert脚本字符串
     * @return 国际化编码集合
     */
    public Set<String> regMatch(String str) {
        String regex = "\\{\\[(.*?)]}";
        // 创建Pattern对象
        Pattern pattern = Pattern.compile(regex);
        // 创建Matcher对象
        Matcher matcher = pattern.matcher(str);
        // 使用Stream API提取匹配到的值并加入到Set中
        return matcher.results()
                .map(matchResult -> matchResult.group(1))
                .collect(Collectors.toSet());
    }
}
