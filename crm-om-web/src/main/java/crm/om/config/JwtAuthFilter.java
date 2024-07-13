package crm.om.config;

import cn.hutool.json.JSONUtil;
import crm.om.utils.JwtHelper;
import crm.om.vo.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 身份验证过滤器
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    @Value("${jwt.authName}")
    private String authName;

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
        final String authHeader = request.getHeader(authName);
        // token 不存在，直接放行
        String token = jwtHelper.parseHeader(authHeader);
        log.info("> [{}] {}, Request Token: {}", request.getMethod(), request.getRequestURI(), token);
        try {
            if (token != null) {
                // 校验token(不通过抛异常)
                jwtHelper.verified(token);
                // 解析用户名
                String userName = jwtHelper.parseUserName(token);

                if (StringUtils.isNotBlank(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                    // 更新身份令牌
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    log.info("{}[{}] -> Roles: {}", userName, jwtHelper.parseUserId(token), userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT Authentication Error ->", e);
            handleException(response, e);
        }
    }

    /**
     * 过滤器异常捕获返回
     *
     * @param response 响应信息
     * @param e        异常
     */
    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JSONUtil.toJsonStr(Result.fail(e.getMessage())));
    }
}

