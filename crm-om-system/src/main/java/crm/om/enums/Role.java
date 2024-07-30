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
    ROLE_COMMON("ROLE_COMMON", "普通用户"),
    ROLE_ADMIN("ROLE_ADMIN", "管理员"),
    ROLE_SUPER("ROLE_SUPER", "超级管理员");

    /**
     * 状态码
     */
    @EnumValue
    private final String code;
    /**
     * 状态信息
     */
    private final String message;
}
