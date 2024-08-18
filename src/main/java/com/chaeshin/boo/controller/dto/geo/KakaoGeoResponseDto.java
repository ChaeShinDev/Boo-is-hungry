package com.chaeshin.boo.controller.dto.geo;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoGeoResponseDto {

    private ResponseMetaDto meta;

    private List<ResponseDocumentDto> documents;
}
