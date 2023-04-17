package com.fish.springunittestexample.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应
 *
 * @author dayuqichengbao
 * @version 创建时间 2023/4/16 09:30
 * @version  2023/04/16
 */
@Data
@NoArgsConstructor
public class Response<T> {

    private int errno;

    private String errmsg;

    private T data;

    public static <T> Response<T> ok(T data) {
        Response<T> response = new Response<>();
        response.data = data;
        response.errmsg = "ok";
        return response;
    }

}
