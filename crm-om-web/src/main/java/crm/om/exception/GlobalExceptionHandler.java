package crm.om.exception;

import crm.om.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public Result<Object> error(Exception e) {
        log.error("Exception Logger Console ↓ ", e);
        return Result.fail(e.toString());
    }
}