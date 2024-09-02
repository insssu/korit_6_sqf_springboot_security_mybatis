package com.study.springSecurity.repository;

import com.study.springSecurity.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findById(Long id);
    User findByUsername(String username);
    int save(User user);
    int deleteById(Long id);

}
