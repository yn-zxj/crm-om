package crm.om.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import crm.om.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 角色信息
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_role_info")
public class RoleInfo extends BaseInfo {
    @Serial
    private static final long serialVersionUID = -7828012155736840191L;

    /**
     * 角色id
     */
    @TableId(value = "roleId", type = IdType.ASSIGN_ID)
    private String roleId;

    /**
     * 角色名称
     */
    @TableField(value = "roleName")
    private String roleName;

    /**
     * 角色编码
     *
     * @see Role
     */
    @TableField(value = "roleCode")
    private String roleCode;

    /**
     * 角色描述
     */
    @TableField(value = "roleDesc")
    private String roleDesc;

    /**
     * 状态(0-弃用 1-启用)
     */
    @TableField(value = "status")
    private String status;
}
