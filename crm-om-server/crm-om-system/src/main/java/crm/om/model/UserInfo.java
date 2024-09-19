package crm.om.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import crm.om.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

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
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private String userId;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 性别
     *
     * @see Gender
     */
    @TableField(value = "user_gender")
    private String userGender;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 手机号码
     */
    @TableField(value = "user_phone")
    private String userPhone;

    /**
     * 邮箱地址
     */
    @TableField(value = "user_email")
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
