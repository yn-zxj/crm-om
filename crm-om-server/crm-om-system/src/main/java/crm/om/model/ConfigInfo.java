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
 * 系统参数配置表
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_config_info")
public class ConfigInfo extends BaseInfo {
    @Serial
    private static final long serialVersionUID = -1599765406324125816L;

    /**
     * 主键ID
     */
    @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    private String configId;

    /**
     * 参数名称
     */
    @TableField(value = "config_name")
    private String configName;

    /**
     * 参数键名
     */
    @TableField(value = "config_key")
    private String configKey;

    /**
     * 参数键值
     */
    @TableField(value = "config_value")
    private String configValue;

    /**
     * 参数类型
     */
    @TableField(value = "config_type")
    private String configType;

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
