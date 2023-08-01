package com.yu.openapi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.common.model.entity.UserInterfaceInfo;

/**
* @author yj
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2023-07-31 10:51:08
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    boolean invokeInterfaceCount(long userId, long interfaceInfoId);

    void getResource(Long userId, long id);

    boolean isFirst(Long userId, long id);

    boolean invokeInterfaceCountDelete(long interfaceInfoId, long userId);
}
