package crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * SpringBoot 启动类
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = "crm.om")
@ServletComponentScan
public class OmWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmWebApplication.class, args);
    }

}
