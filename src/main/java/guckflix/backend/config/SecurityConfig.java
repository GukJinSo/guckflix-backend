package guckflix.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
 */
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.addFilter(corsFilter());
//        http.csrf()
//                .disable();
//        http.authorizeRequests()
//                .antMatchers("/").authenticated();
//        http.formLogin()
//                .disable();
//        http.logout()
//                .logoutUrl()
//                .logoutSuccessHandler()
//                .
//        return http.build();
//    }

    /**
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
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
