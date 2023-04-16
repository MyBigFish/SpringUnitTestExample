package com.fish.springunittestexample.util;

import com.fish.springunittestexample.entity.Response;

import java.util.Collections;

/**
 * 响应工具类
 *
 * @author shulongliu
 * @version 创建时间 2023/4/16 09:34
 * @date 2023/04/16
 */
public class ResponseUtil {
    public static Response successWithData(Object data) {
        Response instance = successWithOutData();
        instance.setData(data);
        return instance;
    }

    public static Response successWithOutData() {
        Response instance = new Response(0,"ok");
        return instance;
    }

    public static Response errorEmptyObjectDataResponse(int errno,String errmsg) {
        Response instance = new Response(errno,errmsg);
        instance.setData(new Object());
        return instance;
    }

    public static Response errorEmptyArrayDataResponse(int errno,String errmsg) {
        Response instance = new Response(errno,errmsg);
        instance.setData(Collections.EMPTY_LIST);
        return instance;
    }
}
