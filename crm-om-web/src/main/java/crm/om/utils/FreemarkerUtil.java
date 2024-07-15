package crm.om.utils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Component
public class FreemarkerUtil {

    private static final Configuration CFG;

    static {
        // 初始化FreeMarker配置
        CFG = new Configuration(Configuration.VERSION_2_3_33);
        // 设置模板目录为 resources/templates/
        CFG.setTemplateLoader(new ClassTemplateLoader(FreemarkerUtil.class, "/templates"));
        CFG.setWhitespaceStripping(true);
        CFG.setDefaultEncoding("UTF-8");
    }

    /**
     * 解析模板文件
     *
     * @param templateName 模板文件名
     * @param model        数据模型
     * @return 解析后的内容
     * <br/>
     * <p>示例：</p>
     * <pre>
     *     public static void main(String[] args) throws TemplateException, IOException {
     *         String process = process("prodConfig/bss.ftl", Map.of("name", "zhang", "age", 20));
     *         System.out.println("process1 = " + process);
     *     }
     * </pre>
     */
    public static String process(String templateName, Map<String, Object> model)
            throws IOException, TemplateException {
        if (templateName == null) {
            return null;
        }
        // 获取模板
        Template template = CFG.getTemplate(templateName);
        StringWriter out = new StringWriter();
        // 处理模板并输出到StringWriter
        template.process(model, out);
        return out.toString();
    }
}
