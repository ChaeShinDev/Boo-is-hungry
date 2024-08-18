package com.chaeshin.boo.controller.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API 명세에 따라 처리 내용을 담은 msg(String)과 data(T data)를 Client 로 전송할 때 사용되는 DTO.
 * <p></p>
 * 1) msg
 * <br></br>
 * 2) data : Request 에 대한 응답으로 데이터가 전달되어야 하는 경우 여기에 데이터가 적재된다.
 */
@EqualsAndHashCode(callSuper = false) // 부모 객체인 BaseResponseDto가 갖고있지 않은 필드인 T data 필드가 있으므로, false로 지정.
@Data
public class DataResponseDto<T>  extends BaseResponseDto {

    private String msg;

    private T data;

    public DataResponseDto(String msg, T data){
        this.msg = msg;
        this.data = data;
    }
}
