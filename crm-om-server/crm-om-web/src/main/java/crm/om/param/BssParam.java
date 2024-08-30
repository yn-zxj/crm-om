package crm.om.param;

import crm.om.enums.ConfigType;
import crm.om.enums.Env;
import crm.om.enums.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * BSS 系统接口入参
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "接口调用参数")
public class BssParam {
    @Schema(description = "环境")
    @NotNull
    private Env env;

    @Schema(description = "平台")
    @NotNull
    private Platform platform;

    @Schema(description = "类型")
    @NotNull
    private ConfigType type;

    @Schema(description = "租户ID(默认：HK)")
    private String tenantId;

    @Schema(description = "语言(默认：zh_CN)")
    private String lang;

    @Schema(description = "接口")
    @NotBlank(message = "接口不能为空")
    private String api;

    @Schema(description = "业务参数")
    @NotNull
    private Object params;
}
