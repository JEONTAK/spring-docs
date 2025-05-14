package com.example.db_search.common.annotation;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
public @interface ResponseTime {
}
