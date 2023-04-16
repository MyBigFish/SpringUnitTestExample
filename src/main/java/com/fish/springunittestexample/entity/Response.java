package com.fish.springunittestexample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应
 *
 * @author shulongliu
 * @version 创建时间 2023/4/16 09:30
 * @version  2023/04/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private int errno;

    private String errmsg;

    private T data;


    public Response(int errno,String errmsg){

        this.errno = errno;
        this.errmsg = errmsg;
    }

}
