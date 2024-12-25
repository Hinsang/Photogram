package com.cos.photogramstart.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
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
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String caption; // 설명문
	private String postImageUrl; // 사진을 전송받아서 그 사진을 서버의 특정 폴더에 저장 - DB에 그 저장된 경로를 insert
	
	@JsonIgnoreProperties({"images"}) // 양방향 매핑에서 해당하는 속성의 정보를 가져오지 않는다. (user안에 있는 images를 무시한다)
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.EAGER) // 이미지를 select하면 조인해서 User 정보를 같이 들고옴
	private User user;
	
	// 이미지 좋아요
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image") // mappedBy는 쉽게 말해서 N쪽에서 fk에 대한 자바 변수를 입력한다
	private List<Likes> likes;
	
	// 이미지 댓글
	@OrderBy("id DESC")
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Comment> comments; // N쪽의 fk 필드는 연관관계의 주인이다.
	
	private LocalDateTime createDate;
	
	@Transient	// DB에 컬럼이 만들어지지 않는다.
	private boolean likeState;
	
	@Transient
	private int likeCount;
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	// syso를 쓸 경우 무한참조 오류 방지 (User 부분을 출력되지 않게 한다)
	/*
	@Override
	public String toString() {	// @Data를 하면 toString()을 포함한다. 엔티티 간에 무한 참조 오류를 막기 위해서 + ", user=" + user 부분을 지워준다.
		return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl 
				+ ", createDate=" + createDate + "]";
	}
	*/
	
}
