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
public enum Status {
    // 状态枚举
    DISABLE(false, "弃用"),
    ENABLE(true, "启用");

    @EnumValue
    @JsonValue
    private final Boolean code;
    private final String desc;
}
