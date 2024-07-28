package crm.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import crm.om.mapper.UserInfoMapper;
import crm.om.model.UserInfo;
import crm.om.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserService {

}
