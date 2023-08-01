package com.yu.openapi.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.common.model.entity.InterfaceInfo;
import com.common.model.entity.InvokeRequest;
import com.common.model.entity.User;
import com.yu.api_client.client.ApiClient;

import com.yu.openapi.annotation.AuthCheck;
import com.yu.openapi.common.*;
import com.yu.openapi.constant.CommonConstant;
import com.yu.openapi.exception.BusinessException;
import com.yu.openapi.exception.ThrowUtils;
import com.yu.openapi.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.yu.openapi.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.yu.openapi.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.yu.openapi.model.dto.userInterfaceInfo.InvokeInterfaceRequest;
import com.yu.openapi.model.enums.InterfaceInfoStatusEnum;
import com.yu.openapi.service.InterfaceInfoService;
import com.yu.openapi.service.UserInterfaceInfoService;
import com.yu.openapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口管理
 *
 * @author yj
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;


    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        String name = interfaceInfoQueryRequest.getName();
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // content 需支持模糊搜索 如果不清空则无法模糊
        interfaceInfoQuery.setDescription(null);
        interfaceInfoQuery.setName(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 上线接口
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if (idRequest == null || id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断接口是否存在
        InterfaceInfo interfaceInfoById = interfaceInfoService.getById(id);
        if (interfaceInfoById == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断接口是否可以调用
        // 更新数据库
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean isSuccessful = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(isSuccessful);
    }

    /**
     * 下线接口
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        if (idRequest == null || id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断接口是否存在
        InterfaceInfo interfaceInfoById = interfaceInfoService.getById(id);
        if (interfaceInfoById == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 更新数据库
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean isSuccessful = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(isSuccessful);
    }

    /**
     * 在线调用接口
     *
     * @param invokeInterfaceRequest 携带id、请求参数
     * @return data
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterface(@RequestBody InvokeInterfaceRequest invokeInterfaceRequest
            , HttpServletRequest request) throws UnsupportedEncodingException {
        if (invokeInterfaceRequest == null || invokeInterfaceRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断接口是否存在
        long id = invokeInterfaceRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(interfaceInfo.getStatus() != InterfaceInfoStatusEnum.ONLINE.getValue()
                , ErrorCode.SYSTEM_ERROR, "接口未上线");
        // 得到当前用户
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        String url = interfaceInfo.getUrl();
        ApiClient client = new ApiClient(accessKey, secretKey, url);
        //判断是否是第一次调用该接口
        boolean first = userInterfaceInfoService.isFirst(loginUser.getId(), id);
        //第一次调用会申请资源
        if (first) {
            try {
                userInterfaceInfoService.getResource(loginUser.getId(), id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 发送请求
        String invokeUserRequestParams = invokeInterfaceRequest.getUserRequestParams();
        InvokeRequest invokeRequest = JSONUtil.toBean(invokeUserRequestParams, InvokeRequest.class);
        String result = client.invokeInterface(invokeRequest);
        return ResultUtils.success(result);
    }

    @GetMapping("/interfaceNameList")
    public BaseResponse<Map> interfaceNameList(){
        List<InterfaceInfo> list = interfaceInfoService.list();
        Map interfaceNameMap=new HashMap();
        for (InterfaceInfo interfaceInfo : list) {
            interfaceNameMap.put(interfaceInfo.getName(),interfaceInfo.getName());
        }
        return ResultUtils.success(interfaceNameMap);
    }
}
