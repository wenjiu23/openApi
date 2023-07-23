package com.yu.openapi.model.dto.postfavour;

import com.yu.openapi.common.PageRequest;
import com.yu.openapi.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子收藏查询请求
 *

 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 帖子查询请求
     */
    private InterfaceInfoQueryRequest postQueryRequest;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}