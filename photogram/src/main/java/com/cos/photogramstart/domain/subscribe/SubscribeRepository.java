package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

	// 네이티브 쿼리
	// :변수로 파라미터에 매핑, nativeQuery = true 안 넣어주면 발동 안됨
	// 데이터에 변경을 주는 네이티브 쿼리에는 @Modifying이 필요하다 (INSERT, UPDATE, DELETE)
	@Modifying
	@Query(value = "INSERT INTO subscribe(fromUserId, toUserId, createDate) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
	void mSubscribe(int fromUserId, int toUserId); // 성공 1(변경된 행의 개수가 리턴됨 10번 성공하면 10이 리턴됨), 실패 -1(오류)
	
	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	void mUnSubscribe(int fromUserId, int toUserId);
	
	// 페이지 유저 정보와 로그인 유저 정보가 구독 테이블에 있으면 1을 리턴(boolean처럼 처리)
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
	int mSubscribeState(int principalId, int pageUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :pageUserId", nativeQuery = true)
	int mSubscribeCount(int pageUserId);
	
}
