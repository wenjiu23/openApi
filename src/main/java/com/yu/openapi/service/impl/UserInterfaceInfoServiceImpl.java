package com.yu.openapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.openapi.common.ErrorCode;
import com.yu.openapi.exception.BusinessException;
import com.yu.openapi.model.entity.UserInterfaceInfo;
import com.yu.openapi.service.UserInterfaceInfoService;
import com.yu.openapi.mapper.UserInterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author yj
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2023-07-31 10:51:08
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
     if(userInterfaceInfo == null){
         throw new BusinessException(ErrorCode.PARAMS_ERROR);
     }
     if(add){
         if(userInterfaceInfo.getInterfaceInfoId()<=0 || userInterfaceInfo.getUserId()<=0){
             throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户或接口不存在");
         }
     }
     if(userInterfaceInfo.getLeftNum()<=0){
         throw new BusinessException(ErrorCode.OPERATION_ERROR,"无调用次数");
     }
    }

    @Override
    public boolean invokeInterfaceCount(long userId, long interfaceInfoId) {
        if (userId <= 0 || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInterfaceInfo::getUserId, userId)
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .gt(UserInterfaceInfo::getLeftNum, 0)
                .setSql("leftNum = leftNum -1, totalNum = totalNum + 1");

        return update(updateWrapper);
    }
}




