package crm.om.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import crm.om.annotation.Log;
import crm.om.model.LogInfo;
import crm.om.service.ILogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

/**
 * 操作日志记录处理
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    private final ILogService logService;

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            LogInfo opLogin = new LogInfo();
            opLogin.setStatus(HttpStatus.HTTP_OK);
            // 请求的地址 TODO
            opLogin.setOpIp("");
            // TODO getRequest().getRequestURI()
            opLogin.setOpUrl("");
            String username = StpUtil.getTokenInfo().tokenName;
            if (StringUtils.isNotBlank(username)) {
                opLogin.setOpName(username);
            }

            if (e != null) {
                opLogin.setStatus(HttpStatus.HTTP_PRECON_FAILED);
                opLogin.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            opLogin.setMethod(className + "." + methodName + "()");
            // 设置请求方式 TODO ServletUtils.getRequest().getMethod()
            opLogin.setRequestMethod("");
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, opLogin, jsonResult);
            // 设置消耗时间
            opLogin.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            // 保存数据库
            logService.save(opLogin);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("handleLog-异常信息: {}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param opLogin 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, LogInfo opLogin, Object jsonResult) throws Exception {
        // 设置action动作
        opLogin.setBusinessType(log.businessType().ordinal());
        // 设置标题
        opLogin.setTitle(log.title());
        // 设置操作人类别
        opLogin.setOpType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, opLogin, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtils.isNotBlank((CharSequence) jsonResult)) {
            opLogin.setOpResult(StringUtils.substring(JSONUtil.toJsonStr(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param opLogin 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, LogInfo opLogin, String[] excludeParamNames) throws Exception {
        String requestMethod = opLogin.getRequestMethod();
        // TODO ServletUtils.getParamMap(ServletUtils.getRequest());
        opLogin.setOpParam("");
    }
}
