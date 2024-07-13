package crm.om.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    /**
     * 枚举说明
     */
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(202, "服务异常"),
    DATASOURCE_ERROR(203, "数据源切换失败"),
    DATA_ERROR(204, "系统数据异常"),
    DATA_ERROR_OR_DATA_NOT_EXIST(204, "系统数据异常或数据不存在"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),
    ARGUMENT_VALID_ERROR(210, "参数校验异常"),
    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),
    TOKEN_ERROR(210, "Token异常"),
    TOKEN_EXPIRE(211, "Token过期"),
    ACCOUNT_ERROR(214, "账号不正确"),
    PASSWORD_ERROR(215, "密码不正确"),
    ACCOUNT_STOP(217, "账号已停用"),
    NO_USER(219, "未找到该用户"),
    ALREADY_USER(220, "该用户名已存在"),
    ALREADY_EMAIL(221, "该邮箱已存在"),
    VERIFY_ERROR(222, "验证码错误"),
    TEMPLATE_NOT_FOUND(223, "模版不存在"),
    CONFIG_NOTFOUND(204, "配置不存在");

    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 状态信息
     */
    private final String message;
}

