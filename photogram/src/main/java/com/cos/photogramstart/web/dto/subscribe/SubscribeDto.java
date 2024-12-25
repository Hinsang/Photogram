package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscribeDto {

	/*
	 	
	 	공부하다가 궁금해서 찾아본 int와 Integer의 차이
	 	● Integer를 사용하는 경우
	 	1. 매개변수로 객체를 필요로 할 때
		2. 기본형 값이 아닌 객체로 저장해야할 때
		3. 객체 간 비교가 필요할 때
	 	-----------------------------------------
	 	int : 자료형(primitive type)

		산술 연산 가능함
		null로 초기화 불가
		-----------------------------------------
		Integer : 래퍼 클래스 (Wrapper class)
		
		Unboxing하지 않을 시 산술 연산 불가능함
		null값 처리 가능
		
		ex) SELECT TRUE FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id를 했을 때,
		TRUE가 NULL을 리턴할 경우 에러가 생길 수 있다.
		
	 */
	
	private int id; // 로그인 유저 아이디
	private String username; // 구독중인 유저 아이디
	private String profileImageUrl; // 구독중인 유저 프로필 사진
	private Integer subscribeState; // 구독 상태
	private Integer equalUserState; // 로그인 유저와 동일한지 확인
	
}
