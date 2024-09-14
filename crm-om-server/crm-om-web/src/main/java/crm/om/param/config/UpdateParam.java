package crm.om.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "更新参数信息")
public class UpdateParam {
    @Schema(description = "配置id", example = "1")
    @NotNull(message = "配置id")
    private Integer configId;

    @Schema(description = "参数名")
    private String paramKey;

    @Schema(description = "参数值")
    private String paramValue;

    @Schema(description = "状态(0-弃用 1-启用)")
    private Integer status;
}
