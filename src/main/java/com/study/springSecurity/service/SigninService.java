package com.study.springSecurity.service;

import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.dto.request.ReqSigninDto;
import com.study.springSecurity.dto.response.RespJwtDto;
import com.study.springSecurity.repository.UserRepository;
import com.study.springSecurity.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SigninService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;

    public RespJwtDto signin(ReqSigninDto dto) {
        // username을 찾으면 user에 값이 들어가고 아니면 Exception이 발생, username 확인
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 다시 입력하세요.")
        );
        // password 확인
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 입력하세요.");  // 비밀번호가 틀렸을 때 발생시킬 예외
        }
        return RespJwtDto.builder().accessToken(jwtProvider.generateUserToken(user)).build();  // 토큰을 만들어서 리턴해 줌
//        System.out.println(user);
//
//        System.out.println("로그인 성공");
    }
}