package com.yu.openapi.service.inner;

import com.common.service.InnerUserInterfaceInfoService;
import com.yu.openapi.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部用户接口信息服务实现类
 *
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeInterfaceCount(interfaceInfoId, userId);
    }

    @Override
    public boolean invokeCountDelete(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeInterfaceCountDelete(interfaceInfoId, userId);
    }
}
