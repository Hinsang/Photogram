package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomValidationApiException extends RuntimeException {

	// 객체를 구분할 때
	private static final long serialVersionUID = 1L;

	private Map<String, String> errorMap;
	
	// 에러를 1개 리턴하는 경우
	public CustomValidationApiException(String message) {
		super(message);
	}
	
	// 에러를 여러개를 리턴하는 경우
	public CustomValidationApiException(String message, Map<String, String> errorMap) {
		super(message);
		this.errorMap = errorMap;
	}

	public Map<String, String> getErrorMap() {
		return errorMap;
	}
	
}
