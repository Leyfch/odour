package com.bjfu.li.odour.common.pojo;


import java.io.Serializable;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

//字段为空的话，不序列化进json数据，即不像前台返回空的值。
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Getter
public class SverResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int state;
    private String msg;
    private T data;

    @JsonIgnore
    public boolean isSuccess() {
        return this.state == ResponseCode.SUCCESS.getCode();
    }

    private SverResponse(int state) {
        this.state = state;
    }

    private SverResponse(int state, T data) {
        this.state = state;
        this.data = data;
    }

    private SverResponse(int state, String msg, T data) {
        this.state = state;
        this.msg = msg;
        this.data = data;
    }

    private SverResponse(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public static <T> SverResponse<T> createRespBySuccess() {
        return new SverResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> SverResponse<T> createRespBySuccess(T data) {
        return new SverResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> SverResponse<T> createRespBySuccess(String msg, T data) {
        return new SverResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> SverResponse<T> createRespBySuccessMessage(String msg) {
        return new SverResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> SverResponse<T> createRespByError() {
        return new SverResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> SverResponse<T> createByErrorMessage(String errorMessage) {
        return new SverResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> SverResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new SverResponse<T>(errorCode, errorMessage);
    }
}

