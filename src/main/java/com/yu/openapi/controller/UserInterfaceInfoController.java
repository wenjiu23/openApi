package com.yu.openapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.model.entity.InterfaceInfo;
import com.common.model.entity.User;
import com.common.model.entity.UserInterfaceInfo;
import com.common.model.vo.SelfInterfaceDateVo;
import com.yu.openapi.annotation.AuthCheck;
import com.yu.openapi.common.*;
import com.yu.openapi.constant.CommonConstant;
import com.yu.openapi.exception.BusinessException;
import com.yu.openapi.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.yu.openapi.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;

import com.yu.openapi.service.InterfaceInfoService;
import com.yu.openapi.service.UserInterfaceInfoService;
import com.yu.openapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.yu.openapi.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 接口管理
 *
 *@author yj
 */
@RestController
@RequestMapping("/userinterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;


    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest,
                                                         HttpServletRequest request) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        // 参数校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        User user = userService.getLoginUser(request);
        System.out.println(user);
        long id = userInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        userService.isAdmin(request);
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(userInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        if (userInterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(userInterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param userInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userInterfaceInfoPage);
    }

    @GetMapping("/selfInterfaceData")
    public BaseResponse<List<SelfInterfaceDateVo>> selfInterfaceData(HttpServletRequest request){
        User currentUser =(User) request.getSession().getAttribute(USER_LOGIN_STATE);
        Long id = currentUser.getId();
        LambdaQueryWrapper<UserInterfaceInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserInterfaceInfo::getUserId,id);
        List<UserInterfaceInfo> list = userInterfaceInfoService.list(lqw);
        List<SelfInterfaceDateVo> selfInterfaceDateVos = new ArrayList<>();

        for (UserInterfaceInfo userInterfaceInfo : list) {
            SelfInterfaceDateVo selfInterfaceDateVo = new SelfInterfaceDateVo();
            BeanUtils.copyProperties(userInterfaceInfo,selfInterfaceDateVo);

            Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
            LambdaQueryWrapper<InterfaceInfo> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(InterfaceInfo::getId,interfaceInfoId);
            InterfaceInfo one = interfaceInfoService.getOne(lqw1);

            String name = one.getName();
            selfInterfaceDateVo.setInterfaceName(name);
            selfInterfaceDateVos.add(selfInterfaceDateVo);
        }
        return ResultUtils.success(selfInterfaceDateVos);
    }

}
