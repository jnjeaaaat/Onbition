package org.jnjeaaaat.onbition.service.impl.user;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.DUPLICATED_USER_NAME;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NO_AUTHORITY;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.jnjeaaaat.onbition.domain.dto.user.UserDto;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyRequest;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyResponse;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.ImageService;
import org.jnjeaaaat.onbition.service.UserService;
import org.jnjeaaaat.onbition.util.JwtTokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final ImageService imageService;
  private final UserRepository userRepository;
  private final JwtTokenUtil jwtTokenUtil;

  /*
  [유저정보변경]
  Request: 유저 PK, MultipartFile 이미지, 유저 이름
  Response: 유저 PK, 유저 id, 유저 이름, 유저 프로필사진, 유저 변경날짜
   */
  @Override
  @Transactional
  public UserModifyResponse updateUser(Long userId, MultipartFile image, UserModifyRequest request) {
    log.info("[updateUser] 유저 정보 변경 - 유저 id : {}", userId);
    // 토큰 정보의 user 와 요청하는 userId 가 다를때
    if (!Objects.equals(userId, jwtTokenUtil.getUserIdFromToken())) {
      throw new BaseException(NO_AUTHORITY);
    }

    // 유저 정보 추출
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

    if (image != null && !image.isEmpty()) {
      log.info("[updateUser] 프로필 사진 변경");
      updateUserProfileImg(user, image);
    }
    if (!Objects.equals(user.getName(), request.getName())) {
      log.info("[updateUser] 유저 이름 변경");
      updateUserName(user, request.getName());
    }

    return UserModifyResponse.from(
        UserDto.from(user)
    );
  }

  /*
  프로필 사진 업데이트
   */
  private void updateUserProfileImg(User user, MultipartFile image) {
    // 원래 이미지 삭제
    String oldFile = user.getProfileImgUrl();
    imageService.deleteImage(oldFile);

    // 새로운 이미지 저장
    String newFile = imageService.saveImage(image, FileFolder.PROFILE_IMAGE);
    user.setProfileImgUrl(newFile);
  }

  /*
  유저 이름 업데이트 (변경시)
   */
  private void updateUserName(User user, String name) {
    // 이름 중복
    if (userRepository.existsByNameAndDeletedAtNull(name)) {
      throw new BaseException(DUPLICATED_USER_NAME);
    }

    user.setName(name);
  }

}
