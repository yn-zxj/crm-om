package crm.om.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum Platform {
    // 系统平台枚举
    BSS(1, "bss"),
    MVNE(2, "mvne"),
    MVNO(3, "mvno");

    private final Integer code;
    @EnumValue
    @JsonValue
    private final String desc;
}
