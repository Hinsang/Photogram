package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// NotNull = Null값 체크
// NotEmpty = 반값, Null 체크
// NotBlank = 빈값, Null, 빈 공백(스페이스)까지 체크

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	
	@NotBlank // 빈값, null, 빈 공백 체크
	private String content;
	@NotNull // Null값 체크
	private Integer imageId;
	
	// toEntity가 필요 없다.
	
}
