package guckflix.backend.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guckflix.backend.dto.MemberDto;
import guckflix.backend.exception.ResponseDto;
import guckflix.backend.security.authen.PrincipalDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 로그인 성공 시 메세지를 보낼 핸들러
 {
 "status": "OK",
 "message": "Login OK",
 "status_code": 200
 }
 */
@Component
public class ApiAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain
    chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Long userId = ((PrincipalDetails) authentication.getPrincipal()).getMember().getId();
        String role = ((PrincipalDetails) authentication.getPrincipal()).getMember().getRole().toString();

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDto success = new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK, "Login OK", new MemberDto.User(userId, role));
        response.setStatus(HttpStatus.OK.value());
        String json = objectMapper.writeValueAsString(success);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }
}
