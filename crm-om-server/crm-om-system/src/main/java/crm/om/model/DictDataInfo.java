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
 * 字典数据表
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict_data")
public class DictDataInfo extends BaseInfo {

    @Serial
    private static final long serialVersionUID = -5401926325266157204L;

    /**
     * 主键
     */
    @TableId(value = "dict_code", type = IdType.ASSIGN_ID)
    private String dictCode;

    /**
     * 字典排序
     */
    @TableField(value = "dict_sort")
    private String dictSort;

    /**
     * 字典标签
     */
    @TableField(value = "dict_label")
    private String dictLabel;

    /**
     * 字典键值
     */
    @TableField(value = "dict_value")
    private String dictValue;

    /**
     * 字典类型
     *
     * @see DictTypeInfo#dictType
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
