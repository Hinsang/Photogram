package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${file.path}") // apllication.yml에 설정한 file.path 이미지 파일 경로가 그대로 들어온다.
	private String uploadFolder; // 주소를 별도로 대입해서 특정 서비스에서 주소 변경도 가능하다.
	
	@Transactional
	public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile) {
		
		UUID uuid = UUID.randomUUID(); // 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약, 파일에 고유한 랜덤 ID를 부여한다. 중복되는 경우를 방지하기 위해 파일 명 앞에 "_"를 붙여준다.
		String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename(); // 1.jpg
		System.out.println("이미지 파일이름 : " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder + imageFileName); // 저장될 경로와 파일 이름을 넣어준다. (절대 경로 정의)
		
		// 통신, ㅑ/O -> 예외가 발생할 수 있기 때문에 예외처리를 해준다.
		try {
			Files.write(imageFilePath, profileImageFile.getBytes()); // 실제 파일을 서버 폴더에 저장한다. 각각의 파라미터에 파일 경로와 파일을 Byte화 해서 넣어준다.
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("유저를 찾을 수 업습니다.");
		});
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
		
	} // 더티체킹으로 업데이트 됨
	
	@Transactional(readOnly = true) // readOnly = true로 변경하면 변경된 내용을 감지하지 않고 현재의 데이터를 읽어온다.
	public UserProfileDto 회원프로필(int pageUserId, int principalId) {
		
		UserProfileDto dto = new UserProfileDto(); // 로그인 세션과 GetMapping의 Model의 ID를 비교하기 위해 생성자를 만들어준다.
		
		// SELECT * FROM image WHERE userId = :userId;
		User userEntity = userRepository.findById(pageUserId).orElseThrow(() -> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId); // 1은 페이지의 주인, -1은 주인이 아님
		dto.setImageCount(userEntity.getImages().size());
		
		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState == 1); // boolean으로 변경하기 위해 int 1을 true로 전환
		dto.setSubscribeCount(subscribeCount);
		
		// 좋아요 카운트 추가하기
		userEntity.getImages().forEach((image)->{
			image.setLikeCount(image.getLikes().size());
		});
		
		return dto;
		
	}
	
	@Transactional
	public User 회원수정(int id, User user) {
		
		// 영속화
		// 결과값이 Null일 때 orElseThrow()로 예외를 처리한다.
		User userEntity = userRepository.findById(id).orElseThrow(() -> {

			return new CustomValidationApiException("찾을 수 없는 id입니다.");
			
		});
		
		userEntity.setName(user.getName());
		
		// 패스워드를 암호화 해서 다시 저장
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		
		return userEntity;
		
	} // 더티체킹이 일어나서 자동으로 업데이트가 완료된다.
	
}
