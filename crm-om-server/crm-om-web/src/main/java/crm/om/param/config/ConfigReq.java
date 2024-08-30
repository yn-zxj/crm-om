package crm.om.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Builder
@Schema(description = "配置信息")
public class ConfigReq {
    @Schema(description = "配置ID")
    private Integer configId;

    @Schema(description = "系统平台")
    private String platform;

    @Schema(description = "环境")
    private String env;

    @Schema(description = "参数名称")
    private String paramName;

    @Schema(description = "参数键")
    private String paramKey;

    @Schema(description = "参数值")
    private Object paramValue;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建人")
    public String createBy;

    @Schema(description = "创建时间")
    public String createTime;

    @Schema(description = "更新人")
    public String updateBy;

    @Schema(description = "更新时间")
    public String updateTime;
}
