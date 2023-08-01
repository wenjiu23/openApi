package com.yu.openapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.model.entity.InterfaceInfo;


/**
* @author yj
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-07-23 19:03:29
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
}
