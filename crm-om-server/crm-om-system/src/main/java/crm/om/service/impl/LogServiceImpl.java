package crm.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import crm.om.mapper.LogInfoMapper;
import crm.om.model.LogInfo;
import crm.om.service.ILogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo> implements ILogService {

}
