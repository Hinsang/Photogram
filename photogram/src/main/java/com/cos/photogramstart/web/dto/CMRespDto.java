package com.cos.photogramstart.web.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CMRespDto<T> {

	private int code; // 1(성공), -1(실패)
	private String message;
	// 제네릭으로 넘겨주는 값에 따라서 자료형 변경
	private T data;
	
}
