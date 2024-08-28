package com.study.springSecurity.security.principal;

import com.study.springSecurity.domain.entity.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// 시큐리티는 유저라는 개체 자체를 가질 수는 없음. Authentication 이라는 객체를 통해서 인증관리를 함.
// 이 안에 principal이 있고, 또 이 안에 두가지 객체로 나뉘어 principal 을 관리함.
// username, password, authorities(권한)
@Builder
@Data
public class PrincipalUser implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Set<Role> roles;    // principalUser 를 생성할 때 roles 만 넣어주면 된다

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        for (Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return authorities;
//        아래 한줄을 풀어서 쓰면 위의 주석과 같음
        return roles.stream()   // stream() 은 map 을 돌릴 수 있다. collection은 list 와 set만 받기 때문에 map을 돌려주기 위해서 stream을 사용함.
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());   // stream 을 다시 set으로 바꿔준 과정
        // roles 들을 stream 으로 바꿔라, 그리고 이 스트림을 map으로 반복 돌려라. 기존에 있는 것들을 다른것으로 바꿔서 stream 으로 넣어준 뒤(기존의 stream 을 새로운 배열로 만드는 것)
        // role 을 하나 꺼내서 반복을 돌릴거다. 이 롤을 simpleGrantedAuthority로 바꿀건데, 그 role 의 name을 simpleGrantedAuthority에 넣어줄 것이다.
        // 그 후에 자료형을 set으로 바꿨다.
    }

    // 임시 계정
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    // 잠긴 계정 ( 비밀번호 5회 오류, 휴면계정 등 )
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 인증 만료 ( 일정 주기마다 비밀번호 변경 )
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 활성화의 유무 ( 이메일 인증 등이 남았을 경우 )
    @Override
    public boolean isEnabled() {
        return true;
    }
}
