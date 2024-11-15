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
public enum DataBase {
    // 数据库前缀
    PROD("proddb", "产品库"),
    CRM("crmdb", "用户库"),
    BASE("basedb", "基础库"),
    MARKET("marketdb", "营销库"),
    ORDER("obtsdb", "订单库");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;
}
