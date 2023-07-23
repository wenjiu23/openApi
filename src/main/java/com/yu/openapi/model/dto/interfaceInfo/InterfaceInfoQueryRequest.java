package com.yu.openapi.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yu.openapi.common.PageRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String request_header;

    /**
     * 响应头
     */
    private String response_header;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;



    private static final long serialVersionUID = 1L;
}