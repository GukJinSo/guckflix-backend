package guckflix.backend.service;

import guckflix.backend.entity.Member;
import guckflix.backend.entity.enums.MemberRole;
import guckflix.backend.exception.MemberDuplicateException;
import guckflix.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static guckflix.backend.dto.MemberDto.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public String save(Post form){

        List<Member> findMember = memberRepository.findByUsername(form.getUsername());
        if (findMember.size() != 0) throw new MemberDuplicateException("already exist id");

        form.setPassword(passwordEncoder.encode(form.getPassword()));
        Member member = Member.builder()
                .username(form.getUsername())
                .password(form.getPassword())
                .email(form.getEmail())
                .role(MemberRole.USER).build();
        memberRepository.save(member);
        return member.getUsername();
    }
}
