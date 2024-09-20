package crm.om.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum ConfigType {
    // 环境枚举
    GATEWAY_URL("1", "网关地址"),
    DATABASE("2", "数据库配置"),
    TABLE("3", "产品配置表"),
    NOC_URL("4", "网元地址");

    @EnumValue
    private final String code;
    private final String desc;
}
