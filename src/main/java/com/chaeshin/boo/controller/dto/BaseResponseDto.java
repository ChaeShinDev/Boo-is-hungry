package com.chaeshin.boo.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 모든 Response DTO의 근간이 되는 DTO. 말은 거창하지만 String 형의 msg 필드만 가지고 있을 뿐이다.
 * <br></br>
 * setter 를 통해 데이터를 추가하는 것도 가능하도록 @NoArgsConstructor 를 추가해주었다.
 */
@Data
@NoArgsConstructor
public class BaseResponseDto {
    private String msg;

    public BaseResponseDto(String msg){
        this.msg = msg;
    }
}
