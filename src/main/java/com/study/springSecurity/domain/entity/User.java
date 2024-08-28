package com.study.springSecurity.domain.entity;

import com.study.springSecurity.security.principal.PrincipalUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity  // table로 만듦
@Data
@Builder
public class User {
    @Id  // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment
    private Long id;  // id변수명은 정해져있음
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

    // fetch: 엔티티를 조인했을 때 연관된 데이터를 언제 가져올지 결정(EAGET - 당장, LAYZ - 나중에 사용할 때
    // many to many를 하게 되면 무조건 join테이블을 만들어줘야함
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)  // master테이블 위에 붙이는 어노테이션, join테이블을 만들어줌
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),  // User테이블의 id를 JPA가 바꿔줌
            inverseJoinColumns = @JoinColumn(name = "role_id")  // 외래키
    )
    private Set<Role> roles;  // 중복을 제거하기 위해 사용, 만약 순서가 중요하면 List로 가져와서 중복을 따로 제거해줘야 함

//    @OneToMany(mappedBy = "user")
//    private Set<UserRole> userRoles = new HashSet<>();  // 나중에 select를 위해서 미리 초기화(nullpointException을 방지하기 위해 만듦)

    public PrincipalUser toPrincipalUser() {
        return PrincipalUser.builder()
                .userId(id)
                .username(username)
                .password(password)
                .roles(roles)       // 계정에 부여된 권한들. security는
                .build();
    }
}