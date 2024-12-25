package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int userId) {
		
		// 레파지토리에서 @Query로 네이티브 쿼리로 저장하면 객체가 아니라 인트를 리턴받기 때문에 네이티브 쿼리 사용이 불가능하다.
		
		// JpaRepository의 save()로 수동 저장
		// Tip (객체를 만들 때 id값만 담아서 insert 할 수 있다), findById로 찾아서 Optional 형식으로 넣는 방식과 비슷하다.
		// 대신 return 시에 image 객체와 user 객체는 id값만 가지고 있는 빈 객체를 리턴받는다.
		Image image = new Image();
		image.setId(imageId);
		
		// User 엔티티의 id(fk id)를 제외한 다른 정보들도 가지고 와야할 때
		User userEntity = userRepository.findById(userId).orElseThrow(() -> {
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		// Entity를 직접 넣어줘서 fk의 id 뿐만 아니라 해당 id의 모든 정보들을 다 가져온다.
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
		
	}
	
	@Transactional
	public void 댓글삭제(int id) {
		try {
			commentRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
		
	}
	
}
