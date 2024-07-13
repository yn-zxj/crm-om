package crm.om.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum Role {
    // 角色枚举
    ROLE_COMMON(1, "普通用户"),
    ROLE_ADMIN(2, "管理员"),
    ROLE_SUPER(3, "超级管理员");

    /**
     * 状态码
     */
    @EnumValue
    private final Integer code;
    /**
     * 状态信息
     */
    private final String message;
}
