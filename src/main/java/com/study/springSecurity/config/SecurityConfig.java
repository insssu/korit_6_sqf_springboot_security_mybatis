package com.study.springSecurity.config;

import com.study.springSecurity.security.filter.JwtAccessTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity  // 우리가 만든 SecurityConfig를 적용시키겠다.
@Configuration  // configuration 어노테이션이 달려 있으면 이 클래스 안에서 Bean등록을 할 수 있음
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAccessTokenFilter jwtAccessTokenFilter;

    @Bean  // Bean 등록, 컴포넌트는 메서드의 이름으로 IoC에 등록이 됨
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable();  // formLogin() : form 태그로 만들어진 로그인화면이 만들어져 있음
        http.httpBasic().disable();  // httpBasic() : alert창에 로그인이 뜸(??)
        http.csrf().disable();  // csrf : 위조 방지 스티커(토큰), SSR에 주로 사용

//        Spring Security가 세션을 생성하지도 않고 기존의 세션을 사용하지도 않겠다.
//        http.sessionManagement().disable();

//        Spring Security가 세션을 생성하지 않겠다. 기존의 세션을 완전히 사용하지 않겠다는 뜻은 아님(인증에서 세션을 사용하지 않겠다.)
//        JWT 등의 토근 인증방식을 사용할 때 설정하는 것
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // crossOrigin 사용, 서버가 서로 다를 때 요청을 날리기 위한 것, 다른 서버에서 요청이 들어오면 특정 주소나 메서드로 제한 가능
        http.cors();  // config폴더에 WebMvcConfig를 만들어서 세팅을 해줘야 함
        http.authorizeRequests()
                .antMatchers("/auth/**", "/h2-console/**")  // 주소를 선택
                .permitAll()  // 모든 권한을 줌
                .anyRequest()  // 나머지 요청
                .authenticated()  // 인증을 거쳐라
                .and()  // iFrame 연결을 안하는 것
                .headers()
                .frameOptions()
                .disable();

        // filter의 순서, UsernamePasswordAuthenticationFilter필터 전에 jwtAccessTokenFilter필터를 추가
        http.addFilterBefore(jwtAccessTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}

// security 라이브러리를 설정하기 위한 것이 config