package crm.om.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建Swagger配置
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {
    /**
     * 自定义设置
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
        };
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("CRM_ISF运维系统接口协议")
                        .version("1.0")
                        .contact(new Contact().name("zhangxj_a_sc").email("zhangxj_a_sc@si-tech.com.cn"))
                        .description("CRM_ISF运维系统接口协议")
                        .license(new License().name("Apache 2.0")
                                .url("https://doc.xiaominfo.com")));
    }
}
