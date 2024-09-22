package crm.om.param.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "字典类型新增参数")
public class DictTypeAddParam {
    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
