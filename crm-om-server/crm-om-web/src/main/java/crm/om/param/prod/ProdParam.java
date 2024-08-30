package crm.om.param.prod;


import crm.om.enums.Env;
import crm.om.enums.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.LinkedHashSet;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "产品配置查询参数")
public class ProdParam {
    @Schema(description = "环境")
    @NotNull
    private Env env;

    @Schema(description = "平台")
    @NotNull
    private Platform platform;

    @Schema(description = "资费ID列表")
    @NotNull
    private LinkedHashSet<String> prcId;
}
