package crm.om.aspect;

import ch.qos.logback.core.util.StringUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import crm.om.annotation.Log;
import crm.om.enums.Constant;
import crm.om.filter.PropertyPreExcludeFilter;
import crm.om.model.LogInfo;
import crm.om.service.ILogService;
import crm.om.utils.IpUtils;
import crm.om.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    private final ILogService logService;

    private final static Integer SUCCESS = 0;
    private final static Integer EXCEPTION = 1;

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
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "opResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object opResult) {
        handleLog(joinPoint, controllerLog, null, opResult);
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

    /**
     * 日志写表
     *
     * @param joinPoint     切点
     * @param controllerLog 拦截注解
     * @param e             异常
     * @param opResult      结果
     */
    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object opResult) {
        try {
            LogInfo opLogin = new LogInfo();
            opLogin.setStatus(SUCCESS);
            // 请求的地址
            opLogin.setOpIp(IpUtils.getIpAddr());
            opLogin.getOpTime("");
            HttpServletRequest request = ServletUtils.getRequest();
            opLogin.setOpUrl(request != null ? StringUtils.substring(request.getRequestURI(), 0, 255) : "");
            String userid = (String) StpUtil.getLoginId();
            if (StringUtils.isNotBlank(userid)) {
                opLogin.setOpName(userid);
            }

            if (e != null) {
                opLogin.setStatus(EXCEPTION);
                opLogin.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            opLogin.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            opLogin.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, opLogin, opResult);
            // 设置消耗时间(毫秒)
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
     * @param log      日志
     * @param opLogin  操作日志
     * @param opResult 返回结果
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, LogInfo opLogin, Object opResult) {
        // 设置action动作
        opLogin.setBusinessType(log.businessType().ordinal());
        // 设置标题
        opLogin.setTitle(log.title());
        // 设置操作人类别
        opLogin.setOpType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中
            setRequestValue(joinPoint, opLogin, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && opResult != null) {
            opLogin.setOpResult(StringUtils.substring(JSONUtil.toJsonStr(opResult), 0, 2000));
        }
    }


    /**
     * 获取请求的参数，放到log中
     *
     * @param opLog 操作日志
     */
    private void setRequestValue(JoinPoint joinPoint, LogInfo opLog, String[] excludeParamNames) {
        String requestMethod = opLog.getRequestMethod();
        HttpMethod method = HttpMethod.valueOf(requestMethod);
        Set<HttpMethod> allowedMethods = Set.of(HttpMethod.PUT, HttpMethod.POST);
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        if (!paramsMap.isEmpty() && allowedMethods.contains(method)) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            opLog.setOpParam(StringUtils.substring(params, 0, 2000));
        } else {
            opLog.setOpParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (!StringUtil.isNullOrEmpty((String) o) && !isFilterObject(o)) {
                    String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                    params.append(jsonObj).append(Constant.Symbol.SPACE);
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
