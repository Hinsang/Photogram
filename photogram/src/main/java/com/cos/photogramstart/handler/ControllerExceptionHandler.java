package com.cos.photogramstart.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController
@ControllerAdvice	// 모든 예외를 낚아채서 실행한다. 
public class ControllerExceptionHandler {
	
	@ExceptionHandler(CustomValidationException.class)	// 예외를 가로채는 함수 설정
	public String validationException(CustomValidationException e) {
		// <?> 제네릭은 값을 넘겨주는 자료형에 따라 자동으로 자료형을 변환해서 넘겨준다.
		// CMRespDto, Script 비교
		// 1. 클라이언트에게 응답할 때는 Script가 좋음
		// 2. Ajax통신 - CMRespDto
		// 3. Android통신 - CMRespDto
		
		if(e.getErrorMap() == null) {
			// CustomValidationException에서 ErrorMap()을 null로 넘겨주는 경우에는 toString()을 사용하지 못하기 때문에 분기 처리를 해준다.
			return Script.back(e.getMessage());
		} else {
			return Script.back(e.getErrorMap().toString()); // 만들어놓은 Script를 보내준다.
		}
		
	}
	
	@ExceptionHandler(CustomException.class)	// 예외를 가로채는 함수 설정
	public String exception(CustomException e) {
		return Script.back(e.getMessage());
	}
	
	@ExceptionHandler(CustomValidationApiException.class)	// 예외를 가로채는 함수 설정
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
		// 오류 상태일 때 HttpStatus의 상태 코드를 오류 코드로 보내주기 위해서 ResponseEntity<?>를 사용하고 리턴할 때 HttpStatus.BAD_REQUEST를 보내준다.
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)	// 예외를 가로채는 함수 설정
	public ResponseEntity<?> apiException(CustomApiException e) {
		// 오류 상태일 때 HttpStatus의 상태 코드를 오류 코드로 보내주기 위해서 ResponseEntity<?>를 사용하고 리턴할 때 HttpStatus.BAD_REQUEST를 보내준다.
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
}
