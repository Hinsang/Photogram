package com.cos.photogramstart.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor	// 생성자를 만들어 final 변수를 초기화 할 수 있게 만들어준다. @Autowired의 빈 컨테이너 할당과 다른 방식
@Service
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	// 패스워드는 스프링부트에서 알아서 체킹하기 때문에 신경쓸 필요가 없다.
	// 리턴이 잘되면 자동으로 UserDetails 타입을 세션으로 만든다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			return null;
		} else {
			return new PrincipalDetails(userEntity); // principalDetails에 유저 정보를 넣어서 보내준다
		}
		
	}

}
