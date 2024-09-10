package crm.om.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import crm.om.enums.Gender;
import crm.om.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * 用户信息
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user_info")
public class UserInfo extends BaseInfo {
    @Serial
    private static final long serialVersionUID = -16108333546115085L;

    /**
     * 用户id
     */
    @TableId(value = "userId", type = IdType.ASSIGN_ID)
    private String userId;

    /**
     * 用户名
     */
    @TableField(value = "userName")
    private String userName;

    /**
     * 性别
     *
     * @see Gender
     */
    @TableField(value = "userGender")
    private String userGender;

    /**
     * 昵称
     */
    @TableField(value = "nickName")
    private String nickName;

    /**
     * 手机号码
     */
    @TableField(value = "userPhone")
    private String userPhone;

    /**
     * 邮箱地址
     */
    @TableField(value = "userEmail")
    private String userEmail;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 状态(1-正常 0-禁用)
     */
    @TableField(value = "status")
    private String status;
}
