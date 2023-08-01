package com.yu.openapi.controller;

import com.common.model.vo.InvokeInterfaceInfoVO;
import com.yu.openapi.common.BaseResponse;
import com.yu.openapi.common.ResultUtils;
import com.yu.openapi.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chart")
public class ChartController {

	@Resource
	private ChartService chartService;

	@GetMapping("/top/interface/invoke")
	BaseResponse<List<InvokeInterfaceInfoVO>> listTopInvokeInterfaceInfo () {
		List<InvokeInterfaceInfoVO> listTopInvokeInterfaceInfo = chartService.listTopInvokeInterfaceInfo(3);
		return ResultUtils.success(listTopInvokeInterfaceInfo);
	}
}