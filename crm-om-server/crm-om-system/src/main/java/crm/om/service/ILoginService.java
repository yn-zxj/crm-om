package crm.om.service;

import com.baomidou.mybatisplus.extension.service.IService;
import crm.om.model.LoginInfo;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
public interface ILoginService extends IService<LoginInfo> {
    /**
     * 记录登录信息
     *
     * @param userId  用户ID
     * @param status  登录状态
     * @param msgInfo 消息内容
     */
    public void recordLoginInfo(String userId, Integer status, String msgInfo);
}
