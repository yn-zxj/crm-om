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
public enum Env {
    // 环境枚举
    TEST("test", "测试"),
    PROD("prod", "生产"),
    ALL("all", "全部");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    @Override
    public String toString() {
        return this.code + ":" + this.desc;
    }
}
