package guckflix.backend.security;

import guckflix.backend.entity.Member;
import guckflix.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 시큐리티 설정에서 loginProcessingUrl이 실행되는 위치
 * loginProcessingUrl에 요청이 오면 자동으로 UserDetailsService 타입 객체의 loadUserByUsername 함수가 실행됨
 */
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 시큐리티 세션에 들어갈 수 있는건 Authentication. Authentication 안에 UserDetails 타입이 들어감
     * 시큐리티 세션(내부 Authentication(내부 UserDetails))
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByUsername(username);
        if(memberEntity != null){
            return new PrincipalDetails(memberEntity);
        }
        return null;
    }
}

