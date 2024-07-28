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
 * 菜单与角色关系表
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_menu_role")
public class MenuRoleRel extends BaseInfo {

    @Serial
    private static final long serialVersionUID = -3090914989371542279L;

    /**
     * 唯一id
     */
    @TableId(value = "uuid", type = IdType.ASSIGN_ID)
    private String uuid;

    /**
     * 菜单id
     */
    @TableField(value = "menuId")
    private String menuId;

    /**
     * 角色id
     */
    @TableField(value = "roleId")
    private String roleId;
}