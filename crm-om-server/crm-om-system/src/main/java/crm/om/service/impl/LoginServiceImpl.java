package crm.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import crm.om.mapper.LoginInfoMapper;
import crm.om.model.LoginInfo;
import crm.om.service.ILoginService;
import crm.om.utils.IpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo> implements ILoginService {

    @Override
    public void recordLoginInfo(String userId, Integer status, String msgInfo) {
        LoginInfo loginInfo = LoginInfo.builder()
                .userId(userId)
                .ip(IpUtils.getIpAddr())
                .status(status)
                .msg(msgInfo)
                .accessTime(LocalDateTime.now())
                .build();
        this.save(loginInfo);
    }
}
