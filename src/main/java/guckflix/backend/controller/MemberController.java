package guckflix.backend.controller;


import guckflix.backend.dto.MemberDto;
import guckflix.backend.security.authen.PrincipalDetails;
import guckflix.backend.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = {"회원 API"})
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    @ApiOperation(value = "회원가입", notes = "MemberDto로 회원을 가입한다")
    public ResponseEntity join(@ModelAttribute MemberDto form){
        String savedUsername = memberService.save(form);
        return ResponseEntity.ok(savedUsername);
    }

    /**
     * 테스트
     */
    @GetMapping("/members")
    public String members(@AuthenticationPrincipal PrincipalDetails member) {
        return member.getMember().toString();
    }
}
