package eu.europa.ec.etrustex.admin.old.web;

import eu.europa.ec.etrustex.admin.old.web.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;


@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>(userSecurityService());

        http
                .authorizeRequests()
                .antMatchers("/", "/login.do", "/j_security_check", "/accessDenied", "/forbidden",
                        "/outOfSession", "/resources/**", "/styles/**", "/images/**", "/scripts/**", "/pagescripts/**").permitAll()
                .anyRequest().authenticated()
                .and().jee().authenticatedUserDetailsService(wrapper)

                // to allow POST requests. TODO include csrf token instead https://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html#csrf-include-csrf-token
                .and().csrf().disable();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.expressionHandler(new DefaultWebSecurityExpressionHandler() {
//            @Override
//            protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
//                WebSecurityExpressionRoot root = (WebSecurityExpressionRoot) super.createSecurityExpressionRoot(authentication, fi);
//                root.setDefaultRolePrefix(""); //remove the prefix ROLE_
//                return root;
//            }
//        });
//    }

    @Bean
    UserDetailsService userSecurityService() {
        return new CustomUserDetailsService();
    }
}
