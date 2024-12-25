package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; // Repository는 EntityManager를 구현해서 만들어져 있는 구현체, Repository 모델과 다른 새로운 형식을 리턴받기 위해서 사용한다.
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId, int pageUserId) {
		
		// 쿼리 준비
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profileimageUrl, "); // 끝에 띄어쓰기를 넣어줘야 한다.
		sb.append("if ((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) subscribeState, ");
		sb.append("if ((?=u.id), 1, 0) equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?"); // 세미콜론 첨부하면 안됨

		// 1. 물음표 principalId (로그인 유저)
		// 2. 물음표 principalId (로그인 유저)
		// 3. 물음표 pageUserId (페이지 주인)
		
		// 쿼리 완성
		Query query = em.createNativeQuery(sb.toString()) // 자바 퍼시스턴스 쿼리를 임포트 해야한다.
			.setParameter(1, principalId)
			.setParameter(2, principalId)
			.setParameter(3, pageUserId);
		
		// 쿼리 실행 (qlrm 라이브러리 필요 = DTO에 DB결과를 매핑하기 위해서)
		JpaResultMapper result = new JpaResultMapper(); // 데이터베이스에서 result된 결과를 자바 클래스에 매핑해주는 라이브러리 사용(qlrm 라이브러리 pom.xml에 등록)
		// 쿼리를 넣고 SubscribeDto.class로 리턴을 받는다.
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class); // 1개면 uniqueResult, 여러개면 list를 사용한다.
		
		return subscribeDtos;
		
	}
	
	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
		
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
	
}
