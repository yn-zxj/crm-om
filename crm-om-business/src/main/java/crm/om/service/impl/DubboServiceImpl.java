package crm.om.service.impl;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Service;

/**
 * Dubbo 服务消费工具类
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Service
public class DubboServiceImpl {

    /**
     * 调用服务
     *
     * @param interfaceName 接口名
     * @param request       接口请求报文
     * @return 接口返回报文
     */
    public static String callService(String interfaceName, String request) {
        interfaceName = interfaceName.replace("_", ".");
        String methodName = interfaceName.substring(interfaceName.lastIndexOf(".") + 1);
        interfaceName = interfaceName.substring(0, interfaceName.lastIndexOf("."));
        return invoke(interfaceName, methodName, request);
    }

    public static String invoke(String interfaceName, String method, String request) {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("generic-call-consumer");
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://137.0.17.206:3181,137.0.17.207:3181,137.0.17.208:3181");
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(interfaceName);
        applicationConfig.setRegistry(registryConfig);
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setGeneric("true");
        referenceConfig.setAsync(true);
        referenceConfig.setTimeout(7000);
        GenericService genericService = referenceConfig.get();

        Object result = genericService.$invoke(method, new String[]{"java.lang.String"}, new String[]{request});

        System.err.println("invokeSayHello(return): " + result);

        return "Ok";
    }

    public static void main(String[] args) {
        String out = callService("com_sitech_ordersvc_common_comp_inter_IBindHomeTownNumberCoSvc_checkSmsOfHomeNo",
                "{\"ROOT" +
                        "\":{\"HEADER\":{\"ROUTING" +
                        "\":{\"ROUTE_KEY\":\"\",\"ROUTE_VALUE\":\"\"},\"TENANT_ID\":\"HK\",\"TRACE_ID\":\"\"," +
                        "\"HSF_GROUP\":\"daijf\"},\"BODY\":{\"BUSI_INFO\":{\"HOME_NO\":\"15284533900\"," +
                        "\"CAPTCHA\":\"774836\"}}}}");
        System.out.println("out = " + out);
    }
}
