package crm.om.vo.dict;

import crm.om.vo.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典数据")
public class DictDataVo extends BaseVo {

    @Schema(description = "主键")
    private String dictCode;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典排序")
    private String dictSort;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "字典键值")
    private Object dictValue;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private Object remark;
}
