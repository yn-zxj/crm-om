package crm.om.param.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "字典数据新增参数")
public class DictDataAddParam {
    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典排序")
    private String dictSort;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "字典键值")
    private String dictValue;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
