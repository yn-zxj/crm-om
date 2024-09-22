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
@Schema(description = "字典类型")
public class DictTypeVo extends BaseVo {

    @Schema(description = "主键")
    private String dictId;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
