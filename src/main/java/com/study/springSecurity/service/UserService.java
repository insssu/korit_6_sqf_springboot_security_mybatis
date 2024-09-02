package com.study.springSecurity.service;

import com.study.springSecurity.domain.entity.Role;
import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.dto.request.ReqSignupDto;
import com.study.springSecurity.dto.response.RespSignupDto;
import com.study.springSecurity.exception.SignupException;
import com.study.springSecurity.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SignupException signupException;

    @Transactional(rollbackFor = SignupException.class)
    public RespSignupDto insertUserAndUserRoles(ReqSignupDto dto) throws SignupException {
        User user = null;
        try {
            user = dto.toEntity();
            userMapper.save(user);

        }
    }
}
