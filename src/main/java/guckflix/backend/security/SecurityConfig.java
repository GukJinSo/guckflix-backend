package guckflix.backend.security;

import guckflix.backend.security.authen.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService oauth2UserService;
    private final ApiAuthenticationSuccessHandler successHandler;
    private final ApiAuthenticationEntryPoint entryPoint401;
    private final ApiAuthenticationFailureHandler failuerHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilter(corsFilter());
        http.csrf()
                .disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/movies/**").authenticated()
                .antMatchers(HttpMethod.POST,"/members/**").hasRole("ADMIN")
                .anyRequest().permitAll();
        http.httpBasic()
                .disable();
        http.formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureHandler(failuerHandler);
        http.oauth2Login()
                .defaultSuccessUrl("http://localhost:3000/",true)
                .userInfoEndpoint()
                .userService(oauth2UserService); // ?????? ????????? ?????? ??????
        http.logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID");
        http.exceptionHandling()
                .authenticationEntryPoint(entryPoint401);
        return http.build();
    }

    /**
     * CORS ??????
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // ??? ????????? ?????? ??? ??????????????? ???????????? ??? ??????
        config.addAllowedOrigin("http://localhost:3000/"); // ????????? ????????? ??????
        config.addAllowedMethod("*"); // ?????? ????????? ??????
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config); // ????????? cors ?????? ??????
        return new CorsFilter(source);
    }




}
