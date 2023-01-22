package guckflix.backend.controller;


import guckflix.backend.dto.MemberDto.PasswordChangePatch;
import guckflix.backend.dto.MemberDto.Post;
import guckflix.backend.security.authen.PrincipalDetails;
import guckflix.backend.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = {"회원 API"})
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    @ApiOperation(value = "회원가입", notes = "아이디, 비밀번호, 이메일로 회원가입한다")
    public ResponseEntity join(@Validated @ModelAttribute Post form, BindingResult br) throws BindException {
        if (br.hasErrors()) {
            throw new BindException(br);
        }
        String savedUsername = memberService.save(form);
        return ResponseEntity.ok(savedUsername);
    }

    @PatchMapping("/member/password-change")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호를 변경한다")
    public ResponseEntity passwordChange(@Validated @ModelAttribute PasswordChangePatch form){
        return ResponseEntity.ok("OK");
    }


}
