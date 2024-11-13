package crm.om.filter;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import crm.om.enums.Constant;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@WebFilter(filterName = "dsFilter", urlPatterns = {"/bssApi/*"})
public class DynamicDatasourceFilter implements Filter {

    private static final ThreadLocal<String> requestBodyCache = new ThreadLocal<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("loading filter {}", filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String requestURI = request.getRequestURI();
        log.info("数据源过滤器拦截, 当前路径是: {}", requestURI);

        String dsKey = null;
        String method = request.getMethod();
        String contentType = request.getContentType();

        if ("POST".equalsIgnoreCase(method) && "application/json".equalsIgnoreCase(contentType)) {
            String requestBody = getRequestBody(request);
            JSON parse = JSONUtil.parse(requestBody);
            Object env = parse.getByPath("env");
            Object platform = parse.getByPath("platform");

            dsKey = env + Constant.Symbol.SHORT_LINE + platform;
        } else if ("GET".equalsIgnoreCase(method)) {
            Object env = request.getParameter("env");
            Object platform = request.getParameter("platform");
            dsKey = platform + Constant.Symbol.SHORT_LINE + env + Constant.Symbol.SHORT_LINE + "basedb";
        }

        // 执行
        try {
            DynamicDataSourceContextHolder.push(dsKey);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            DynamicDataSourceContextHolder.poll();
            clear();
        }
    }

    /**
     * 获取请求体
     *
     * @param request 请求对象
     * @return 请求体
     * @throws IOException 异常
     */
    public static String getRequestBody(HttpServletRequest request) throws IOException {
        if (requestBodyCache.get() == null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
                String requestBody = reader.lines().collect(Collectors.joining());
                requestBodyCache.set(requestBody);
            }
        }
        return requestBodyCache.get();
    }

    /**
     * 清空请求体缓存
     */
    public static void clear() {
        requestBodyCache.remove();
    }
}