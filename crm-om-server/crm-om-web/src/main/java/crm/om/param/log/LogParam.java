package crm.om.param.log;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.LinkedHashSet;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "日志查询参数")
public class LogParam {
    @Schema(description = "日志ID列表")
    @NotNull
    private LinkedHashSet<String> id;
}
