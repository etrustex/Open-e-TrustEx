package eu.europa.ec.etrustex.admin.old.web.security;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActiveUser {
}
