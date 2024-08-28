package com.study.springSecurity.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  // 프로그램 실행 중에 어노테이션 적용시킴(어노테이션을 적용시키는 시점)
@Target({ElementType.METHOD})  // 메서드에 해당 어노테이션을 달 수 있음
public @interface Test2Aop {

}
