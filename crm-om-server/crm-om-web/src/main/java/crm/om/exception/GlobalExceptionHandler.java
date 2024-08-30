package crm.om.exception;

import cn.dev33.satoken.exception.*;
import cn.hutool.core.util.StrUtil;
import crm.om.enums.ResultCode;
import crm.om.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类(针对Controller)
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 未登录异常
     *
     * @param exception NotLoginException
     * @return 异常提示
     */
    @ExceptionHandler(NotLoginException.class)
    public Result<Object> handlerException(NotLoginException exception) {
        return Result.fail(ResultCode.LOGIN_AUTH);
    }

    /**
     * 缺少权限异常
     *
     * @param exception NotPermissionException
     * @return 异常提示
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<Object> handlerException(NotPermissionException exception) {
        return Result.fail(ResultCode.PERMISSION);
    }

    /**
     * 缺少角色异常
     *
     * @param exception NotRoleException
     * @return 异常提示
     */
    @ExceptionHandler(NotRoleException.class)
    public Result<Object> handlerException(NotRoleException exception) {
        return Result.fail(ResultCode.ROLE_ERROR);
    }

    /**
     * 二级认证校验失败异常
     *
     * @param exception NotSafeException
     * @return 异常提示
     */
    @ExceptionHandler(NotSafeException.class)
    public Result<Object> handlerException(NotSafeException exception) {
        return Result.fail(ResultCode.NOT_SAFE);
    }

    /**
     * 服务封禁异常
     *
     * @param exception DisableServiceException
     * @return 异常提示
     */
    @ExceptionHandler(DisableServiceException.class)
    public Result<Object> handlerException(DisableServiceException exception) {
        return Result.fail(StrUtil.format(ResultCode.ACCOUNT_DISABLED.getMessage(), exception.getService(), exception.getLevel(), exception.getDisableTime()));
    }

    /**
     * Http Basic 校验失败异常
     *
     * @param exception NotBasicAuthException
     * @return 异常提示
     */
    @ExceptionHandler(NotBasicAuthException.class)
    public Result<Object> handlerException(NotBasicAuthException exception) {
        return Result.fail(ResultCode.NOT_BASIC_AUTH);
    }

    /**
     * 参数校验
     *
     * @param exception MethodArgumentNotValidException
     * @return 参数校验错误原因
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handlerException(MethodArgumentNotValidException exception) {
        Map<String, String> result = new HashMap<>(16);
        BindingResult bindingResult = exception.getBindingResult();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            FieldError fieldError = (FieldError) objectError;
            log.error("参数 {} = {} 校验错误：{}", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            result.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return Result.fail(result);
    }

    /**
     * 全局异常
     *
     * @param exception Exception
     * @return 异常原因
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handlerException(Exception exception) {
        log.error("Exception Logger Console ↓ ", exception);
        return Result.fail(exception.toString());
    }
}