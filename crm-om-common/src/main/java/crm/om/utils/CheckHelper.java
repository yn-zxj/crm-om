package crm.om.utils;

import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

/**
 * 校验工具类
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Component
public class CheckHelper {

    /**
     * 检验字符串是否为JSON类型
     *
     * @param jsonStr 需要检验的字符串
     * @return 是否为JSON类型
     */
    public boolean isValidJson(String jsonStr) {
        try {
            JSONUtil.parse(jsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
