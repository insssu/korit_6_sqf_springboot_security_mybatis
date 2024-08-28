package com.study.springSecurity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReqSigninDto {
    @NotBlank(message = "사용자 이름을 입력하세요.")  // 아무것도 입력을 안했을 때 발생하는 에러 메시지
    private String username;
    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;
}
