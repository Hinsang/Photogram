package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomApiException extends RuntimeException {

	// 객체를 구분할 때
	private static final long serialVersionUID = 1L;

	private Map<String, String> errorMap;
	
	// 에러를 1개 리턴하는 경우
	public CustomApiException(String message) {
		super(message);
	}
	
}
