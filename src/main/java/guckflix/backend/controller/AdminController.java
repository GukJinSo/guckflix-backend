package guckflix.backend.controller;

import guckflix.backend.service.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = {"관리자 API"})
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

//    @PostMapping("/login")
//    @ApiOperation(value = "관리자 로그인", notes = "아이디와 비밀번호를 검사하여 관리자 로그인")
//    public ResponseEntity login(@Validated @ModelAttribute MemberDto.LoginForm form, BindingResult br) throws BindException {
//        if (br.hasErrors()) {
//            throw new BindException(br);
//        }
//        //String savedUsername = memberService.save(form);
//        return ResponseEntity.ok("ok");
//    }

}
