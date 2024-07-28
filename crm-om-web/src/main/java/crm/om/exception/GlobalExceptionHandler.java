package crm.om.exception;

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
    @ExceptionHandler(Exception.class)
    public Result<Object> error(Exception exception) {
        // 参数校验异常
        if (exception instanceof MethodArgumentNotValidException) {
            Map<String, String> result = new HashMap<>();
            BindingResult bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                FieldError fieldError = (FieldError) objectError;
                log.error("参数 {} = {} 校验错误：{}", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
                result.put(fieldError.getField(), fieldError.getDefaultMessage());
                return Result.fail(result);
            }
        }

        log.error("Exception Logger Console ↓ ", exception);
        return Result.fail(exception.toString());
    }
}