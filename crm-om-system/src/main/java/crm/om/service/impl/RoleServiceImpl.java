package crm.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import crm.om.mapper.RoleInfoMapper;
import crm.om.model.RoleInfo;
import crm.om.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfo> implements IRoleService {

}
