package crm.om.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录记录
 *
 * @author zhangxiaojun
 * @version 1.0
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_login_log")
public class LoginInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -1599765206324125816L;

    /**
     * 访问ID
     */
    @TableId(value = "info_id", type = IdType.ASSIGN_ID)
    private String infoId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * ip
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 操作状态（1正常 0异常）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 提示消息
     */
    @TableField(value = "msg")
    private String msg;

    /**
     * 访问时间
     */
    @TableField(value = "access_time")
    private LocalDateTime accessTime;
}
