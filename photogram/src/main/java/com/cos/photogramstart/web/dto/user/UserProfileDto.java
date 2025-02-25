package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
	
	private boolean pageOwnerState; // 이 페이지의 주인인가?
	private int imageCount;
	private boolean subscribeState;
	private int subscribeCount;
	private User user;
	
}
