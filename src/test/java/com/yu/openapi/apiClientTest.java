package com.yu.openapi;

import cn.hutool.http.HttpUtil;
import com.yu.api_client.client.ApiClient;
import com.yu.api_client.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@SpringBootTest
public class apiClientTest {
    @Resource
    private ApiClient apiClient;

    @Test
    public void test() throws UnsupportedEncodingException {
        String name="yj";
        System.out.println("get:"+apiClient.getNameByGet(name));
        System.out.println("post:"+apiClient.getNameByPost(name));
        User user = new User();
        user.setUserName(name);
        String usernameByPost = apiClient.getUsernameByPost(user);
        System.out.println(usernameByPost);
    }
    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get("http://localhost:8123/api/name/get", paramMap);
        System.out.println(result);
        return result;
    }

}
