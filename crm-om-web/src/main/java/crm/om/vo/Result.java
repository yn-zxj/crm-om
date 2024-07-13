package crm.om.vo;

import crm.om.enums.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局统一返回结果类
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /**
     * 返回码
     */
    @Schema(description = "返回码")
    private String code;
    /**
     * 返回消息
     */
    @Schema(description = "返回消息")
    private String msg;
    /**
     * 返回数据
     */
    @Schema(description = "返回数据")
    private T data;

    /**
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 结果
     */
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    /**
     * @param body    返回数据
     * @param code    返回码
     * @param message 返回消息
     * @param <T>     数据类型
     * @return 结果
     */
    public static <T> Result<T> build(T body, String code, String message) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    /**
     * @param body       返回数据
     * @param resultCode 返回码及返回消息枚举，参考{@link ResultCode}
     * @param <T>        数据类型
     * @return 结果
     */
    public static <T> Result<T> build(T body, ResultCode resultCode) {
        Result<T> result = build(body);
        result.setCode(resultCode.getCode().toString());
        result.setMsg(resultCode.getMessage());
        return result;
    }

    /**
     * 操作成功
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 成功信息
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = build(data);
        return build(data, ResultCode.SUCCESS);
    }

    /**
     * 操作成功
     *
     * @param <T> 数据类型
     * @return 成功信息
     */
    public static <T> Result<T> ok() {
        return Result.ok(null);
    }

    /**
     * 操作失败
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 失败信息
     */
    public static <T> Result<T> fail(T data) {
        Result<T> result = build(data);
        return build(data, ResultCode.FAIL);
    }

    /**
     * 操作失败
     *
     * @param <T> 数据类型
     * @return 失败信息
     */
    public static <T> Result<T> fail() {
        return Result.fail(null);
    }

    public Result<T> message(String msg) {
        this.setMsg(msg);
        return this;
    }

    public Result<T> code(String code) {
        this.setCode(code);
        return this;
    }
}
