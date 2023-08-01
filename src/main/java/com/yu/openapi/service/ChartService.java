package com.yu.openapi.service;

import com.common.model.vo.InvokeInterfaceInfoVO;

import java.util.List;

public interface ChartService {
    List<InvokeInterfaceInfoVO> listTopInvokeInterfaceInfo(int limit);
}
