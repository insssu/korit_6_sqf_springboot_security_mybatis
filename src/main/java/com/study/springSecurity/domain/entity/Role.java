package com.study.springSecurity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;  // ROLE_USER, ROLE_ADMIN, ROLE_MANAGER

//    @OneToMany(mappedBy = "role")
//    private Set<UserRole> userRoles = new HashSet<>();
}