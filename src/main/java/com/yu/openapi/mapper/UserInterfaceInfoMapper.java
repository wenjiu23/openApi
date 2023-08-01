package com.yu.openapi.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.model.entity.UserInterfaceInfo;
import com.common.model.vo.InvokeInterfaceInfoVO;

import java.util.List;

/**
* @author yj
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2023-07-31 10:51:08
* @Entity com.yu.openapi.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    List<InvokeInterfaceInfoVO> listTopInvokeInterfaceInfo(int limit);
}




