package com.study.springSecurity.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RespJwtDto {
    private String accessToken;
    private String refreshToken;  // 사용하지 않지만 이런 형식으로 보내줌
}
