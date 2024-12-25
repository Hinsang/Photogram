package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드의 생성자를 만들어준다.
@Controller
public class AuthController {
	
	// 폼에서 넘어오는 로그 확인
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private final AuthService authService; // final은 변경할 수 없지만 @RequiredArgsConstructor로 생성자를 만들어서 초기화 할 수 있다. 
	
	/*
	 * public AuthController(AuthService authService) { this.authService =
	 * authService; }
	 */
	
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	// 회원가입버튼 -> /auth/signup -> /auth/signin
	// 회원가입버튼 X
	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) { // key=value (x-www-urlencoded)
		// @Valid 어노테이션은 Dto안에 설정해준 제약조건 @Size, @NotBlank 등의 제약 조건을 점검한다. (Entity의 제약조건을 점검하는 것과 다르게 Dto 점검)		
		// BindingResult는 @Valid의 유효검사가 실패했을 때 에러를 바인딩한다.
			
		// log.info(signupDto.toString()); // 폼에서 들어오는 문자열 확인
		User user = signupDto.toEntity();
		// log.info(user.toString());
		// 위에서 builder한 값을 엔티티에 넣어서 보내준다. 
		authService.회원가입(user);
		//System.out.println(userEntity);
		return "auth/signin";
		
	}
	
}
