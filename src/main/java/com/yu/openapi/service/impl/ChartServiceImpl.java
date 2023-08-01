package com.yu.openapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.model.entity.InterfaceInfo;
import com.common.model.vo.InvokeInterfaceInfoVO;
import com.yu.openapi.common.ErrorCode;
import com.yu.openapi.exception.BusinessException;
import com.yu.openapi.mapper.UserInterfaceInfoMapper;
import com.yu.openapi.service.ChartService;
import com.yu.openapi.service.InterfaceInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
@Service
public class ChartServiceImpl implements ChartService {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public List<InvokeInterfaceInfoVO> listTopInvokeInterfaceInfo(int limit) {
        List<InvokeInterfaceInfoVO> vos = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(limit);
        if (vos == null || vos.size() == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        // 根据id查询接口名称
        LinkedHashMap<Long, InvokeInterfaceInfoVO> voHashMap = new LinkedHashMap<>(vos.size());
        for (InvokeInterfaceInfoVO vo : vos) {
            voHashMap.put(vo.getId(), vo);
        }
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(InterfaceInfo::getId, voHashMap.keySet());
        List<InterfaceInfo> infoList = interfaceInfoService.list(queryWrapper);

        for (InterfaceInfo interfaceInfo : infoList) {
            voHashMap.get(interfaceInfo.getId()).setName(interfaceInfo.getName());
        }
        return new ArrayList<>(voHashMap.values());
    }
}
