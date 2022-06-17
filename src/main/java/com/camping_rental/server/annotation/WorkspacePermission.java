package com.camping_rental.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WorkspacePermission {
    boolean MasterOnly() default false;
    boolean MemberOnly() default false;
    boolean PublicOnly() default false;
}
