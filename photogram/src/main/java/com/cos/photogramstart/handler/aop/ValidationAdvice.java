package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component // RestController, Service 모든 것들이 Component를 상속해서 만들어져 있음
@Aspect
public class ValidationAdvice {

	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		//System.out.println("web api 컨트롤러 =================");
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				System.out.println("유효성 검사를 하는 함수입니다.");
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성 검사 실패함", errorMap); // 에러들을 리스트 형식으로 넘겨줌 
				}
				
			}
		}
		// proceedingJoinPoint => profile 함수의 모든 곳에 접근할 수 있는 변수
		// profile 함수보다 먼저 실행
		
		return proceedingJoinPoint.proceed(); // 이때 Profile 함수가 실행됨
		
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		//System.out.println("web 컨트롤러 =================");
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				if(bindingResult.hasErrors()) {
					
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						/*
						 * System.out.println("==========================");
						 * System.out.println(error.getDefaultMessage());
						 * System.out.println("==========================");
						 */
					}
					throw new CustomValidationException("유효성 검사 실패함", errorMap); // 에러들을 리스트 형식으로 넘겨줌 
				}
				
			}
		}
		
		return proceedingJoinPoint.proceed();
		
	}
	
}
