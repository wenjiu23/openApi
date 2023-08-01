package com.yu.openapi.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 调用接口参数
 *
 */
@Data
public class InvokeInterfaceRequest implements Serializable {

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 请求参数
	 */
	private String userRequestParams;
}