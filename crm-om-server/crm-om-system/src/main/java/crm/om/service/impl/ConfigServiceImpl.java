package crm.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import crm.om.mapper.ConfigInfoMapper;
import crm.om.model.ConfigInfo;
import crm.om.service.IConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<ConfigInfoMapper, ConfigInfo> implements IConfigService {

}
