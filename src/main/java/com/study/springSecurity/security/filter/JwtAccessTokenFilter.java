package com.study.springSecurity.security.filter;

import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.repository.UserMapper;
import com.study.springSecurity.security.jwt.JwtProvider;
import com.study.springSecurity.security.principal.PrincipalUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.jar.JarException;

@Component
public class JwtAccessTokenFilter extends GenericFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // header에 Authorization이라는 키값으로 데이터가 넘어옴
        // PostMan에서 Authorization탭에 보면 Auth Type이 Bearer Token이 있는데 여기다가 Jwt토큰을 넣어준다.(클라이언트 요청 시)
        // 그럼 Headers에 Authorization Key값이 생기고 value는 Jwt토큰 값이 암호화되서 들어간다
        String bearerAccessToken = request.getHeader("Authorization");

        if (bearerAccessToken == null || bearerAccessToken.isBlank()) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String accessToken = jwtProvider.removeBearer(bearerAccessToken);
        Claims claims = null;
        try {
            claims = jwtProvider.parseToken(accessToken);
            Long userId = ((Integer) claims.get("userId")).longValue();
            User user = userMapper.findById(userId);
            if (user == null) {   // 토큰은 존재하지만 계정이 사라진 경우
                throw new JarException("해당 ID(" + userId + ")의 사용자 정보를 찾지 못했습니다.");
            }
            PrincipalUser principalUser = user.toPrincipalUser();     // 데이터 베이스에서 가져온 유저 객체를 통해 principalUser 를 만들었음
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(), principalUser.getAuthorities()); // principal 객체가 들어와줘야 한다.
    //            System.out.println("예외 발생하지 않음");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            e.printStackTrace();
            filterChain.doFilter(servletRequest, servletResponse);  // filterChain.doFilter : 다음 필터로 넘어가라
            return; // 리턴을 걸지 않으면 또 다음 필터로 넘어가기때문에 무한루프가 걸려버림
        }
//        System.out.println(bearerAccessToken);
        /*
            출력결과
            Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTcyMjY4MTMwMX0.Ine2WI4XbmZ8V6W8vBn2jK08XBP0LdHmKMHMMIFclM0
            앞에 붙어 있는 Bearer는 이 값이 Jwt토큰값이라는 것을 티를 내기 위해 붙여줌
            프론트엔드에서 요청을 날릴 때 앞에 Bearer을 붙여줘야함(형식: Bearer 토큰값)
        */

        /*
            - Spring Security에서는 SecurityContextHolder에 Context객체에 Authentication값이 들어가 있어야 로그인이 됬다고 인식
            - UsernamePasswordAuthenticationFilter은 SecurityContextHolder에 Context객체에 있는 Authentication값을
              보고 있으면 인증할 필요가 없다고 판단하고 다음 필터로 넘어감
            - 폼 로그인은 바로 UsernamePasswordAuthenticationFilter에서 검증하게 되고 다음 필터로 넘어감
            - SecurityContextHolder 의 lifecycle 은 요청시에 생성, 응답시에 소멸한다. 인증이 됐는지 안됐는지 확인하는 역할.
            - 토큰을 가지고 요청을 한 후에 쓸 수 있다면 통과, 아니면 돌려보내는 역할을 하는데, 만료되는 기간이 있음. 임시 출입증 같은 개념

         */
        filterChain.doFilter(servletRequest, servletResponse);  // 다음 필터로 넘어야가 되기 때문에 무조건 넣어줘야 함
    }
}
