package crm.om.exception;

import cn.hutool.json.JSONUtil;
import crm.om.enums.ResultCode;
import crm.om.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
public class AuthHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    /**
     * 拒绝访问(用户认证) {@link AuthenticationEntryPoint#commence}
     *
     * @param request       请求参数
     * @param response      响应参数
     * @param authException 异常信息 (系统用户不存在、被锁定、凭证失效、密码错误等)
     * @throws IOException IO异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 未授权
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, Object> map = new HashMap<>(2);
        map.put("uri", request.getRequestURI());
        map.put("error", "Unauthorized");
        map.put("exception", authException.getMessage());
        response.getWriter().write(JSONUtil.toJsonStr(Result.build(map, ResultCode.ILLEGAL_REQUEST)));
    }

    /**
     * 访问受保护资源时被拒绝 {@link AccessDeniedHandler#handle}
     *
     * @param request               请求参数
     * @param response              响应参数
     * @param accessDeniedException 异常信息
     * @throws IOException IO异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 被拒绝
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Map<String, Object> map = new HashMap<>(2);
        map.put("uri", request.getRequestURI());
        map.put("error", "Forbidden");
        map.put("exception", accessDeniedException.getMessage());
        response.getWriter().write(JSONUtil.toJsonStr(Result.build(map, ResultCode.PERMISSION)));
    }
}
