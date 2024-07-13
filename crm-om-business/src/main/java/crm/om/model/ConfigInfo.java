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
@TableName(value = "config_info")
public class ConfigInfo extends BaseInfo {
    @Serial
    private static final long serialVersionUID = -6017761212425807610L;
    /**
     * ID 自增
     */
    @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    private String configId;

    /**
     * 系统平台
     */
    @TableField(value = "platform")
    private String platform;

    /**
     * 系统环境
     */
    @TableField(value = "env")
    private String env;

    /**
     * 配置类型
     */
    @TableField(value = "param_name")
    private String paramName;

    /**
     * 参数类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 参数名
     */
    @TableField(value = "param_key")
    private String paramKey;

    /**
     * 参数值
     */
    @TableField(value = "param_value")
    private String paramValue;

    /**
     * 状态(0-弃用 1-启用)
     */
    @TableField(value = "status")
    private Integer status;
}