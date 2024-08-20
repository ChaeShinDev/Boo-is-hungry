package com.chaeshin.boo.utils;

import lombok.Data;

@Data
public class ResponseDto<T> {

    private final String msg;
    private final T data;

    public ResponseDto(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }
}
