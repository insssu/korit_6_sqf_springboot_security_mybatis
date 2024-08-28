package com.study.springSecurity.security.jwt;

import com.study.springSecurity.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    // 해당 키 값으로
    private final Key key;

    // key 생성
    // application.yml에 있는 jwt.secret의 비밀번호 값을 가져와서 String으로 변환 후 secret에 담아줌
    public JwtProvider(@Value("${jwt.secret}") String secret) {
        // secret값을 그대로 사용할 수 없기 때문에 BASE64로 디코딩해서 key값으로 변경하고 key에 담아줌
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String removeBearer(String token) {
//        return token.substring(7); // 하드코딩이므로 이렇게 쓰면 안됨
        return token.substring("Bearer ".length());
        // 개발하다 보면 여러가지의 토큰을 사용할 경우가 생기기 때문에 이렇게 적어줌
    }

    // Jwt토근은 JSON 형태(문자열)
    public String generateUserToken(User user) {
        // User기준으로 토큰을 만듦(매개변수 값)

//        String token = Jwts.builder().compact();  // 이것만으로도 토큰이 만들어짐

        // 토큰의 만료시간을 만들어줌
        // 오늘 날짜를 기준으로 숫자로 만들어주고 new Date()안에 넣어주면 Date객체로 변환해줌
        // 해당 숫자는 한 달 동안 유지시켜줌(getTime()은 millis로 값이 나옴
        Date expireDate = new Date(new Date().getTime() + (1000L * 60 * 60 * 24 * 30));

        String token = Jwts.builder()
                .claim("userId", user.getId())  // claim은 key, value를 추가할때 사용
                .expiration(expireDate)
                // key를 통해 서명 = 해당 key값을 통해 인증을 해야함, 기본 암호화 알고리즘은 HS512
                // yml에서 key값을 만들 때 256으로 만들었기 때문에 여기서도 256으로 해줌
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public Claims parseToken(String token) {
        // build 패턴과 동일. 풀어서 쓰면 아랫 코드
//        Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(key)
                .build();

        return jwtParser.parseClaimsJws(token).getPayload();
        // Payload 안에 Claims가 들어있다.
        // 예외처리를 따로 안해주는 실수를 많이 하니까 주의해야 함.
    }
}
