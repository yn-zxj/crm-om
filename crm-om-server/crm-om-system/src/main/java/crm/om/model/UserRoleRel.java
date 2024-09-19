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
 * 用户与角色关系表
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user_role")
public class UserRoleRel extends BaseInfo {

    @Serial
    private static final long serialVersionUID = -5401926395266157204L;

    /**
     * 主键
     */
    @TableId(value = "uuid", type = IdType.ASSIGN_ID)
    private String uuid;
    /**
     * 用户id
     */
    @TableField(value = "user_d")
    private String userid;

    /**
     * 角色id
     */
    @TableField(value = "role_d")
    private String roleId;
}
