package com.yu.openapi.rpc.impl;

import com.yu.openapi.rpc.service.DemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@DubboService
public class RpcDemoServiceImpl implements DemoService {
	@Override
	public String sayHello(String name) {
		System.out.println("Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
		return "Hello " + name;
	}
}