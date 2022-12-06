package guckflix.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.util.StandardCharset;
import guckflix.backend.dto.ErrorDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilter(corsFilter());
        http.csrf()
                .disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/movies/**").authenticated()
                .antMatchers("/members/**").hasRole("ADMIN")
                .anyRequest().permitAll();
        http.httpBasic()
                .disable();
        http.formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler());
        http.logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID");
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint());
        return http.build();
    }

    /**
     * 성공 시, 401 에러 시 핸들러
     * 성공 : AuthenticationSuccessHandler
     * 401 메세지 전달 : AuthenticationEntryPoint 구현 필요
    {
        "status": "OK",
        "message": "Login OK",
        "status_code": 200
    }
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AuthenticationSuccessHandler(){
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
                AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
            }
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                ObjectMapper objectMapper = new ObjectMapper();
                ErrorDto success = new ErrorDto(HttpStatus.OK.value(), HttpStatus.OK, "Login OK");
                response.setStatus(HttpStatus.OK.value());
                String json = objectMapper.writeValueAsString(success);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            }
        };
    }

    /**
     * AuthenticationEntryPoint를 따로 등록해주지 않으면 전부 403 코드로 응답하므로 401 코드를 받을 받을 수 없다.
     * https://velog.io/@park2348190/Spring-Security%EC%9D%98-Unauthorized-Forbidden-%EC%B2%98%EB%A6%AC
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                ObjectMapper objectMapper = new ObjectMapper();
                ErrorDto fail = new ErrorDto(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, "You need to log in"); // Custom error response.
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                String json = objectMapper.writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS 필터
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답 시 자격인증을 받아들일 지 설정
        config.addAllowedOrigin("http://localhost:3000/"); // 리액트 서버만 허용
        config.addAllowedMethod("*"); // 모든 메서드 허용
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config); // 전체에 cors 필터 설정
        return new CorsFilter(source);
    }


}
