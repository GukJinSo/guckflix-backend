package guckflix.backend.security;

import guckflix.backend.security.authen.PrincipalOauth2UserService;
import guckflix.backend.security.handlers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService oauth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AccessDeniedHandler accessDeniedHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

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
                .loginProcessingUrl("/members/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);
        http.oauth2Login()
                .successHandler(authenticationSuccessHandler)
                .defaultSuccessUrl("http://localhost:3000")
                .failureHandler(authenticationFailureHandler)
                .userInfoEndpoint()
                .userService(oauth2UserService);// 구글 로그인 완료 처리할 서비스
        http.logout()
                .logoutUrl("/members/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID");
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // IF_REQUIRED : 스프링 시큐리티가 필요한 경우에 발급
                .maximumSessions(1) // 동시 세션 허용 수
                .maxSessionsPreventsLogin(false); // 동시 로그인 방지 설정 안 함. 후 사용자가 로그인 하면 선 사용자는 만료
        return http.build();
    }

    /**
     * 세션 만료 동작
     */
    @Bean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new ApiSessionInformationExpiredStrategy();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new ApiLogoutSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ApiAccessDeniedHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new ApiAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new ApiAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new ApiAuthenticationSuccessHandler();
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
        config.addAllowedOrigin("https://www.guckflix.site");
        config.addAllowedOrigin("https://guckflix.site");
        config.addAllowedOrigin("https://gukjinso.github.io/");
        config.addAllowedMethod("*"); // 모든 메서드 허용
        config.addAllowedHeader("*");
        config.addExposedHeader("location");
        source.registerCorsConfiguration("/**", config); // 전체에 cors 필터 설정
        return new CorsFilter(source);
    }

}
