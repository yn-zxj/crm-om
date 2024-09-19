package crm.om.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "新增参数信息")
public class SaveParam {
    @Schema(description = "配置类型")
    @NotBlank(message = "配置类型不能为空")
    private String configName;

    @Schema(description = "参数名")
    @NotBlank(message = "参数名不能为空")
    private String configKey;

    @Schema(description = "参数值")
    @NotBlank(message = "参数值不能为空")
    private String configValue;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "参数类型")
    public String configType;
}
