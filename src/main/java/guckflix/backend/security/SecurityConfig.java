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
                // .antMatchers(HttpMethod.POST,"/movies/**").authenticated()
                // .antMatchers(HttpMethod.POST,"/members/**").hasRole("ADMIN")
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
                .userService(oauth2UserService); // 구글 로그인 완료 처리
        http.logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID");
        http.exceptionHandling()
                .authenticationEntryPoint(entryPoint401);
        return http.build();
    }

    /**
     * CORS 필터
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답 시 자격인증을 받아들일 지 설정
        config.addAllowedOrigin("http://localhost:3000/"); // 로컬 리액트 서버 허용
        config.addAllowedOrigin("https://www.guckflix.site/");
        config.addAllowedOrigin("https://guckflix.site/");
        config.addAllowedMethod("*"); // 모든 메서드 허용
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config); // 전체에 cors 필터 설정
        return new CorsFilter(source);
    }




}
