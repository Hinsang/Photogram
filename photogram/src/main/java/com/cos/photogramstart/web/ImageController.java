package com.cos.photogramstart.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.web.dto.image.imageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {
	
	private final ImageService imageService;
	
	@GetMapping({"/", "/image/story"})
	public String story() {
		return "image/story";
	}
	
	@GetMapping("/image/popular")
	public String popular(Model model) {
		
		List<Image> images = imageService.인기사진();
		
		model.addAttribute("images", images);
		
		return "image/popular";
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(imageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		if(imageUploadDto.getFile().isEmpty()) { // 파일은 @Valid, @NotBlank로 검증이 불가능 하기 때문에 .isEmpty()로 비어 있는 값을 확인하고 예외를 발생 시켜줘야 한다.
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null); // 하나의 에러는 에러 Map을 null로 해서 넘겨주지 않는다.
		}
		
		imageService.사진업로드(imageUploadDto, principalDetails);
		return "redirect:/user/" + principalDetails.getUser().getId();
		
	}
	
}
