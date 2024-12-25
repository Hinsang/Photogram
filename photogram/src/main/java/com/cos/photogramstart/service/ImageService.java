package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.imageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진() {
		return imageRepository.mPopular();
	}
	
	@Transactional(readOnly = true) // 영속성 컨텍스트 변경 감지를 해서 더티체킹, flush(반영) X
	public Page<Image> 이미지스토리(int principalId, Pageable pageable) {
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		// 회원으로 로그인 해서 구독한 이미지들을 출력 
		// images에 좋아요 상태 담기
		images.forEach((image) -> {
			
			image.setLikeCount(image.getLikes().size());
			
			// 자신(로그인 사용자)이 구독한 이미지에 좋아요한 사람들 중에서 자신이 좋아요 했는지 찾기
			image.getLikes().forEach((like) -> {
				if(like.getUser().getId() == principalId) {
					image.setLikeState(true);
				}
			});
			
		});
		
		return images;
	}
	
	@Value("${file.path}") // apllication.yml에 설정한 file.path 이미지 파일 경로가 그대로 들어온다.
	private String uploadFolder; // 주소를 별도로 대입해서 특정 서비스에서 주소 변경도 가능하다.
	
	@Transactional
	public void 사진업로드(imageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		
		UUID uuid = UUID.randomUUID(); // 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약, 파일에 고유한 랜덤 ID를 부여한다. 중복되는 경우를 방지하기 위해 파일 명 앞에 "_"를 붙여준다.
		String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename(); // 1.jpg
		System.out.println("이미지 파일이름 : " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder + imageFileName); // 저장될 경로와 파일 이름을 넣어준다.
		
		// 통신, ㅑ/O -> 예외가 발생할 수 있기 때문에 예외처리를 해준다.
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes()); // 실제 파일을 서버 폴더에 저장한다. 각각의 파라미터에 파일 경로와 파일을 Byte화 해서 넣어준다.
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser()); // 값을 넘겨주고 Entity로 변환
		imageRepository.save(image);
		
		// System.out.println(imageEntity); // .toString()을 출력하는 것과 같다
		
	}
	
}
