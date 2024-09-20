package crm.om.vo.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "配置信息")
public class ConfigVo {
    @Schema(description = "配置ID")
    private String configId;

    @Schema(description = "配置类型")
    private String configName;

    @Schema(description = "参数名")
    private String configKey;

    @Schema(description = "参数值")
    private Object configValue;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "参数类型")
    public String configType;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "创建人")
    public String createBy;

    @Schema(description = "创建时间")
    public String createTime;

    @Schema(description = "更新人")
    public String updateBy;

    @Schema(description = "更新时间")
    public String updateTime;
}
