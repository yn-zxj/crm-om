package crm.om.service.impl;

import crm.om.mapper.BaseInfoMapper;
import crm.om.service.IBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements IBaseService {

    private final BaseInfoMapper baseInfoMapper;

    @Override
    public List<Map<String, Object>> baseInfo(String dataSource, String code) {
        return baseInfoMapper.qryBaseInfo(code);
    }

    @Override
    public List<Map<String, Object>> baseMaxInfo(String dataSource, String prefixCode) {
        return baseInfoMapper.qryBaseMax(prefixCode);
    }
}
