package com.cos.photogramstart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer { // web 설정 파일
	
	@Value("${file.path}") // application.yml에 설정한 file.path 경로를 입력한다.
	private String uploadFolder;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		// file:///C:/workspace/springbootwork/upload/
		registry
			.addResourceHandler("/upload/**") // jsp 페이지에서 /upload/** 이런 주소 패턴이 나오면 발동
			.addResourceLocations("file:///"+uploadFolder)
			.setCachePeriod(60*10*6) // 60초 x 10 x 6 = 1시간 동안 캐시를 저장 한다.
			.resourceChain(true) // 발동
			.addResolver(new PathResourceResolver()); // 등록
			
	}
	
}
