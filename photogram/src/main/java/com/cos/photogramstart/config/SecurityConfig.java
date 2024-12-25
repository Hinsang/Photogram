package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity // 해당 파일로 시큐리티를 활성화
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final OAuth2DetailsService oAuth2DetailsService;
	
	// BCrypt로 패스워드 암호화
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// super 삭제 - 기존 시큐리티가 가지고 있는 기능이 다 비활성화됨.
		// super.configure(http);
		http.csrf().disable();
		http.authorizeRequests()
		  .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated()
		  .anyRequest().permitAll()
		  .and()
		  .formLogin()
		  .loginPage("/auth/signin") // GET
		  .loginProcessingUrl("/auth/signin") // POST -> 스프링 시큐리티가 로그인 프로세스 진행
		  .defaultSuccessUrl("/")
		  .and()
		  .oauth2Login() // form 로그인도 하는데, oauth2 로그인도 한다
		  .userInfoEndpoint() // oauth2 로그인을 하면 최종응답을 Access Token이 아닌 회원정보를 바로 받을 수 있다
		  .userService(oAuth2DetailsService);
		
		// csrf 토큰은 시큐리티에서 정보를 받을 때 정보를 준 위치와 사용자를 식별하기 위한 토큰 값이다. 위치와 상관 없이 정상적으로 페이지에 로딩하기 위해서 csrf 토큰 사용을 꺼놓았다.
		// antMatchers()에 해당 하는 URL은 인증 절차를 가진다
		// anyRequest().permitAll() 그 외의 모든 요청은 인증 절차를 거치지 않는다.
		// formLogin()을 할 것인데 그 로그인 페이지는 loginPage()의 URL이다.
		// dfaultSuccessUrl()로 인증 성공시 접근할 페이지를 입력한다.
		
	}
	
}
