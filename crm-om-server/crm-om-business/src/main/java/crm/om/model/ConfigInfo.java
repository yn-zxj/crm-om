package crm.om.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import crm.om.enums.ConfigType;
import crm.om.enums.Env;
import crm.om.enums.Platform;
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
     *
     * @see Platform
     */
    @TableField(value = "platform")
    private Platform platform;

    /**
     * 系统环境
     *
     * @see Env
     */
    @TableField(value = "env")
    private Env env;

    /**
     * 配置类型
     */
    @TableField(value = "param_name")
    private String paramName;

    /**
     * 参数类型
     *
     * @see ConfigType
     */
    @TableField(value = "type")
    private ConfigType type;

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