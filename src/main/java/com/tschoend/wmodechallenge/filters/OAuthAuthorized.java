package com.tschoend.wmodechallenge.filters;

import java.lang.annotation.*;

/**
 * Created by tom on 2015-09-22.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OAuthAuthorized {
}
