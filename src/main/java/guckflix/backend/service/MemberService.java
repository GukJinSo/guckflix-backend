package guckflix.backend.service;

import guckflix.backend.dto.MemberDto;
import guckflix.backend.entity.Member;
import guckflix.backend.entity.enums.MemberRole;
import guckflix.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public String save(MemberDto dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Member member = Member.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .role(MemberRole.USER).build();
        memberRepository.save(member);
        return member.getUsername();
    }
}
