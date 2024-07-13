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
public enum Gender {
    // 性别枚举
    WOMAN(0, "女"),
    MAN(1, "男"),
    UNKNOWN(2, "未知");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;
}
