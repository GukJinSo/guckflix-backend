package guckflix.backend.controller;


import guckflix.backend.dto.MemberDto.passwordChangeForm;
import guckflix.backend.dto.MemberDto.Post;
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

    @PostMapping("/members")
    @ApiOperation(value = "회원가입", notes = "아이디, 비밀번호, 이메일로 회원가입한다")
    public ResponseEntity join(@Validated @ModelAttribute Post form, BindingResult br) throws BindException {
        if (br.hasErrors()) {
            throw new BindException(br);
        }
        String savedUsername = memberService.save(form);
        return ResponseEntity.ok(savedUsername);
    }

    @PostMapping("/members/login")
    @ApiOperation(value = "로그인", notes = "아이디, 비밀번호로 로그인 한다")
    public ResponseEntity login(@Validated @ModelAttribute Post form, BindingResult br) throws BindException {
        if (br.hasErrors()) {
            throw new BindException(br);
        }
        String savedUsername = memberService.save(form);
        return ResponseEntity.ok(savedUsername);
    }

    @DeleteMapping("/members/{userId}")
    @ApiOperation(value = "회원 삭제", notes = "회원을 삭제한다")
    public ResponseEntity delete(@Validated @ModelAttribute passwordChangeForm form){
        return ResponseEntity.ok("OK");
    }


    @PatchMapping("/members/{userId}")
    @ApiOperation(value = "회원 수정", notes = "비밀번호를 제외한 회원정보를 수정한다")
    public ResponseEntity update(@Validated @ModelAttribute passwordChangeForm form){
        return ResponseEntity.ok("OK");
    }



    @PatchMapping("/members/{userId}/password")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호를 변경한다")
    public ResponseEntity password(@Validated @ModelAttribute passwordChangeForm form){
        return ResponseEntity.ok("OK");
    }


}
