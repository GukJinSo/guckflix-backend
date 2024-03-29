package guckflix.backend.security.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import guckflix.backend.dto.MemberDto;
import guckflix.backend.exception.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ApiLogoutSuccessHandler implements LogoutSuccessHandler
{
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDto success = new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK, "LogOut OK");
        response.setStatus(HttpStatus.OK.value());
        String json = objectMapper.writeValueAsString(success);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }
}
