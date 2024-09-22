package crm.om.param.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.LinkedHashSet;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "字典数据查询参数")
public class DictDataParam {
    @Schema(description = "字典数据主键ID")
    @NotNull
    private LinkedHashSet<String> id;
}
