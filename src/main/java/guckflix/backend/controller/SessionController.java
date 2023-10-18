package guckflix.backend.controller;

import guckflix.backend.dto.MemberDto;
import guckflix.backend.exception.ResponseDto;
import guckflix.backend.security.authen.PrincipalDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/session")
public class SessionController {

    /**
     * 클라이언트의 JSESSIONID가 유효한 지 검사
     */
    @GetMapping("/validation")
    public ResponseEntity checkSession(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);

        String jsessionIdFromCookie = null;
        Authentication authentication = null;
        Long userId = null;

        // 세션이 있으면
        if (session != null) {

            // 이용자가 전송한 JSESSIONID 값
            Cookie[] requestCookies = request.getCookies();
            for (Cookie cookie : requestCookies)
                if(cookie.getName().equals("JSESSIONID")) jsessionIdFromCookie = cookie.getValue();

            // 스프링 시큐리티 인증 확인
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = ((PrincipalDetails) authentication.getPrincipal()).getMember().getId();

            if (authentication != null && authentication.isAuthenticated()) {

                // 이용자가 전송한 JSESSIONID가 세션 값과 일치하는 경우
                if (jsessionIdFromCookie.equals(session.getId())) {
                    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK, "유효 세션 성공", new MemberDto.User(userId)));
                }
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new ResponseDto(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, "유효하지 않은 세션"));

    }

    // 세션이 invalid 하면 세션을 삭제하고 메세지를 리턴. 프론트에서는 다시 로그인 필요해짐
    //    @GetMapping("/invalid")
//    public ResponseEntity invalidSession(HttpServletRequest req, HttpServletResponse resp) {
//
//        HttpSession session = req.getSession(true);
//        session.invalidate();
//
//        Cookie cookie = new Cookie("JSESSIONID", null);
//        cookie.setMaxAge(100);
//        resp.addCookie(cookie);
//
//        return ResponseEntity.badRequest().body(new ResponseDto(400, HttpStatus.BAD_REQUEST, "유효하지 않은 세션 ID"));
//
//    }
}
