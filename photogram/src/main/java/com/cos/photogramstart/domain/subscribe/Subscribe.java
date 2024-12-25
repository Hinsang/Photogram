package com.cos.photogramstart.domain.subscribe;

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

import com.cos.photogramstart.domain.user.User;

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
			name = "subscribe_uk",
			columnNames = {"fromUserId", "toUserId"}
		)
	}
)
public class Subscribe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	// User 테이블끼리 구독은 서로 1:N, N:1 관계(Fk가 많은 경우)이기 때문에 테이블을 새로 만들어서 @ManyToOne으로 연결시켜주었다.
	@JoinColumn(name = "fromUserId")	// fk 이름 변경
	@ManyToOne
	private User fromUser; // 팔로우
	
	@JoinColumn(name = "toUserId")
	@ManyToOne
	private User toUser; // 팔로워
	
	private LocalDateTime createDate;
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
}
