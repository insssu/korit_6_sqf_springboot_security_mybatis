package com.study.springSecurity.dto.response;

import com.study.springSecurity.domain.entity.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RespSignupDto {
    private String message;
    private User user;
}
