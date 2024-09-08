package crm.om.config;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import crm.om.model.RoleInfo;
import crm.om.model.UserRoleRel;
import crm.om.service.IRoleService;
import crm.om.service.IUserRoleRelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义权限加载接口实现类
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {
    
    private final IRoleService roleService;
    private final IUserRoleRelService userRoleRelService;

    /**
     * 返回指定账号id所拥有的权限码集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 查询用户具有的角色信息
        LambdaQueryWrapper<UserRoleRel> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(UserRoleRel::getUserid, loginId);
        List<UserRoleRel> roleList = userRoleRelService.list(roleWrapper);

        // 查询角色说明
        LambdaQueryWrapper<RoleInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.in(RoleInfo::getRoleId, roleList.stream().map(UserRoleRel::getRoleId).collect(Collectors.toList()));
        List<RoleInfo> list = roleService.list(infoWrapper);

        return list.stream().map(RoleInfo::getRoleCode).collect(Collectors.toList());
    }
}
