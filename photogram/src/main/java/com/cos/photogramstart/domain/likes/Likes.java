package com.cos.photogramstart.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(	// 연관된 테이블에 Unique속성 추가하기
	uniqueConstraints = {
		@UniqueConstraint(
			name = "likes_uk",
			columnNames = {"imageId", "userId"}
		)
	}
) // columnNames에 고유한 1개의 아이디를 얻을 수 있게 한다. _uk = unique key
public class Likes { // N

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// 무한 참조됨
	@JoinColumn(name = "imageId") // fk
	@ManyToOne
	private Image image; // 1
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId") // fk
	@ManyToOne
	private User user; // 1
	
	private LocalDateTime createDate;
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
}
