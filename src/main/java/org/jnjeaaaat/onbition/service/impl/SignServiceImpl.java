package org.jnjeaaaat.onbition.service.impl;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.ALREADY_REGISTERED_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.DUPLICATED_USER_NAME;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpResponse;
import org.jnjeaaaat.onbition.domain.dto.user.UserDto;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.ImageService;
import org.jnjeaaaat.onbition.service.SignService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 유저 회원가입, 로그인 service interface 구현체 class
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

  private final ImageService imageService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /*
  1. 회원가입
  Request: image 파일, uid, password, name, phone
  Response: user PK, profileImgUrl, uid, name, roles
   */
  @Override
  public SignUpResponse signUp(MultipartFile image, SignUpRequest request) {

    log.info("[signUp] 회원가입 요청");
    // 이미 존재하는 유저일때
    if (userRepository.existsByUidAndDeletedAt(request.getUid(), null)) {
      throw new BaseException(ALREADY_REGISTERED_USER);
    }
    // 유저 이름이 중복될때
    if (userRepository.existsByNameAndDeletedAt(request.getName(), null)) {
      throw new BaseException(DUPLICATED_USER_NAME);
    }

    // 파일 저장 후 url 가져오기
    String imageUrl = imageService.saveImage(image, FileFolder.PROFILE_IMAGE);

    // User -> UserDto -> SignUpResponse form 가공
    return SignUpResponse.from(
        UserDto.from(userRepository.save(User.builder()
                .uid(request.getUid())  // 아이디
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화 저장
                .profileImgUrl(imageUrl)  // image Url 저장 (http://...)
                .name(request.getName())  // 유저 이름
                .phone(request.getPhone()) // 유저 핸드포 번호
                .roles(Collections.singletonList("ROLE_VIEWER")) // 기본 권한 VIEWER
                .build()
            )
        )
    );
  }

}
