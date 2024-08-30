package crm.om.exception;

import crm.om.enums.ResultCode;

import java.io.Serial;

/**
 * 自定义异常
 *
 * @author zhangxiaojun
 * @version 1.0
 */
public class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7295235980399052931L;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(ResultCode info) {
        super(info.getMessage());
    }
}
