package crm.om.param.config;

import crm.om.enums.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "新增参数信息")
public class SaveReq {
    @Schema(description = "系统平台")
    @NotBlank(message = "系统平台不能为空")
    private Platform platform;

    @Schema(description = "系统环境")
    @NotBlank(message = "系统环境不能为空")
    private String env;

    @Schema(description = "配置类型")
    @NotBlank(message = "配置类型不能为空")
    private String paramName;

    @Schema(description = "参数名")
    @NotBlank(message = "参数名不能为空")
    private String paramKey;

    @Schema(description = "参数值")
    @NotBlank(message = "参数值不能为空")
    private String paramValue;
}
