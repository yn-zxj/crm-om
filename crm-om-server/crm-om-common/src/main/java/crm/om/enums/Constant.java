package crm.om.enums;

/**
 * 常量
 *
 * @author zhangxiaojun
 * @version v1.0
 */
public class Constant {
    /**
     * 符号常量
     */
    public static class Symbol {
        public final static String SHORT_LINE = "-";
        public final static String SPLIT_SLASH = "/";
        public final static String BACKQUOTE = "`";
        public final static String COMMA = ",";
        public final static String SPACE = " ";
        public final static String SINGLE_QUOTE = "'";
        public final static String SEMICOLON = ";";
        public final static String LEFT_BRACKET = "(";
        public final static String RIGHT_BRACKET = ")";
        public final static String NEW_LINE = System.lineSeparator();
    }
    
    /**
     * SQL 片段
     */
    public static class Sql {
        public final static String IN = "IN";
        public final static String INSERT_INTO = "INSERT INTO";
        public final static String SELECT_ALL_FROM = "SELECT * FROM";
        public final static String WHERE = "WHERE";
        public final static String VALUES = "VALUES";
    }
}