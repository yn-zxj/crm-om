package crm.om.param.datasource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
public class DataSourceParam {
    @Schema(description = "连接池名称")
    private String poolName;

    @Schema(description = "JDBC driver")
    private String driverClassName;

    @Schema(description = "JDBC url")
    private String url;

    @Schema(description = "JDBC 用户名")
    private String username;

    @Schema(description = "JDBC 密码")
    private String password;
}
