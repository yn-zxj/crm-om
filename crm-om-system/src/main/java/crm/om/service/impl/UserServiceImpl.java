package crm.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import crm.om.enums.ResultCode;
import crm.om.exception.BaseException;
import crm.om.mapper.UserInfoMapper;
import crm.om.model.system.RoleInfo;
import crm.om.model.system.UserInfo;
import crm.om.model.system.UserRoleRel;
import crm.om.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserService, UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = qryUserInfo(UserInfo.builder().userName(username).build());
        if (userInfo != null) {
            // 角色列表
            List<GrantedAuthority> authorities = userInfo.getRoleName().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new User(userInfo.getUserName(), userInfo.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    @Override
    public UserInfo qryUserInfo(UserInfo userInfo) {
        // 查询用户信息
        UserInfo userInfoResult = lambdaQuery()
                .eq(userInfo.getUserName() != null, UserInfo::getUserName, userInfo.getUserName())
                .eq(userInfo.getUserId() != null, UserInfo::getUserId, userInfo.getUserId())
                .one();
        // 用户存在
        if (userInfoResult != null) {
            // 查询用户与角色的关联关系
            List<UserRoleRel> userRoleRelList = Db.lambdaQuery(UserRoleRel.class)
                    .eq(UserRoleRel::getUserid, userInfoResult.getUserId())
                    .list();
            if (!userRoleRelList.isEmpty()) {
                // 查询角色信息
                List<RoleInfo> roleInfoList = Db.lambdaQuery(RoleInfo.class)
                        .in(RoleInfo::getRoleId, userRoleRelList.stream().map(UserRoleRel::getRoleId).collect(Collectors.toList()))
                        .list();
                if (!roleInfoList.isEmpty()) {
                    userInfoResult.setRoleName(roleInfoList.stream().map(RoleInfo::getRoleCode).toList());
                    return userInfoResult;
                } else {
                    throw new BaseException(ResultCode.DATA_ERROR);
                }
            } else {
                throw new BaseException(ResultCode.DATA_ERROR);
            }
        } else {
            throw new BaseException(ResultCode.NO_USER);
        }
    }
}
