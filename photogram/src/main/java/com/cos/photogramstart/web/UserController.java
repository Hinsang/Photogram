package com.cos.photogramstart.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	
	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		UserProfileDto dto = userService.회원프로필(pageUserId, principalDetails.getUser().getId());
		model.addAttribute("dto", dto);
		return "user/profile";
	}
	
	@GetMapping("/user/{id}/update")
	public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		// 세션에 있는 유저 정보 가져오기, Model에 추가해서 프론트로 넘겨주기
		
		// @AuthenticationPrincipal를 이용해서 Session -> SecurityContextHolder -> Authentication -> PrincipalDetails에 바로 접근할 수 있다. (추천 방식)
		// System.out.println("세션 정보 : " + principalDetails.getUser()); 
		
		// 유저 세션 정보에 접근하는 다른 방식 (직접 경로를 찾아서 들어가는 방식)
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// PrincipalDetails mPrincipalDetails = (PrincipalDetails) auth.getPrincipal();
		// System.out.println("직접 찾은 세션 정보 : " + mPrincipalDetails.getUser());
		
		// jstl 태그 라이브러리를 사용할 경우 model에 저장해서 넘길 필요가 없다. 태그 라이브러리에서 UserDetails에 접근하기 때문에
		// model.addAttribute("principal", principalDetails.getUser());
		
		return "user/update";
		
	}
	
}
