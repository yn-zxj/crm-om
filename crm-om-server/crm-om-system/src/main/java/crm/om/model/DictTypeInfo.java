package crm.om.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 字典类型表
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict_type")
public class DictTypeInfo extends BaseInfo {

    @Serial
    private static final long serialVersionUID = -2401926395261157204L;

    /**
     * 主键
     */
    @TableId(value = "dict_id", type = IdType.ASSIGN_ID)
    private String dictId;

    /**
     * 字典名称
     */
    @TableField(value = "dict_name")
    private String dictName;

    /**
     * 字典类型
     *
     * @see DictDataInfo#dictType
     */
    @TableField(value = "dict_type")
    private String dictType;

    /**
     * 状态(0-弃用 1-启用)
     */
    @TableField(value = "status")
    private String status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
}
