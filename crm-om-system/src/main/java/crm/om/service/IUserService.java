package crm.om.service;

import com.baomidou.mybatisplus.extension.service.IService;
import crm.om.model.system.UserInfo;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
public interface IUserService extends IService<UserInfo> {
    /**
     * 用户信息查询
     *
     * @param userInfo 查询条件
     * @return 用户信息
     */
    UserInfo qryUserInfo(UserInfo userInfo);
}
