package org.jnjeaaaat.onbition.service.impl.user;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.DUPLICATED_USER_NAME;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NO_AUTHORITY;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SAME_PASSWORD;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UN_MATCH_PASSWORD;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UN_MATCH_PHONE_NUM;

import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jnjeaaaat.onbition.config.client.SmsClient;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.jnjeaaaat.onbition.domain.dto.user.PasswordModifyRequest;
import org.jnjeaaaat.onbition.domain.dto.user.ResetPasswordRequest;
import org.jnjeaaaat.onbition.domain.dto.user.UserDto;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyRequest;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyResponse;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.ImageService;
import org.jnjeaaaat.onbition.service.UserService;
import org.jnjeaaaat.onbition.config.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 유저 정보 변경, 비밀번호 변경, 조회 Interface 구현체 class
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final SmsClient smsClient;
  private final ImageService imageService;
  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  private final int RANDOM_STRING_LENGTH = 10;

  /*
  [유저정보변경]
  Request: 유저 PK, MultipartFile 이미지, 유저 이름
  Response: 유저 PK, 유저 id, 유저 이름, 유저 프로필사진
   */
  @Override
  @Transactional
  public UserModifyResponse updateUser(Long userId, MultipartFile image, UserModifyRequest request)
      throws IOException {
    log.info("[updateUser] 유저 정보 변경 - 유저 id : {}", userId);
    // 토큰 정보의 user 와 요청하는 userId 가 다를때
    if (!Objects.equals(userId, jwtTokenProvider.getUserIdFromToken())) {
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
  [비밀번호 변경]
  Request: oldPassword, newPassword
  Response: success message
   */
  @Override
  @Transactional
  public void updatePassword(Long userId, PasswordModifyRequest request) {
    log.info("[updateUser] 유저 정보 변경 - 유저 id : {}", userId);
    // 토큰 정보의 user 와 요청하는 userId 가 다를때
    if (!Objects.equals(userId, jwtTokenProvider.getUserIdFromToken())) {
      throw new BaseException(NO_AUTHORITY);
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

    // 비밀번호 다름
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BaseException(UN_MATCH_PASSWORD);
    }
    // 바꿀 비밀번호와 같으면
    if (request.getPassword().equals(request.getNewPassword())) {
      throw new BaseException(SAME_PASSWORD);
    }

    // 비밀번호 변경
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
  }

  /*
  [비밀번호 초기화]
  Request: 유저 id, 유저 핸드폰번호
  Response: success message
   */
  @Override
  @Transactional
  public void resetPassword(ResetPasswordRequest request) {
    log.info("[resetPassword] 비밀번호 초기화 - id : {}", request.getUid());
    // 유저 정보 추출
    User user = userRepository.findByUidAndDeletedAtNull(request.getUid())
        .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

    // 핸드폰 번호가 다를때
    if (!user.getPhone().equals(request.getPhone())) {
      throw new BaseException(UN_MATCH_PHONE_NUM);
    }

    // 랜덤 문자열
    String randomPassword = getRandomPassword();
    log.info("[resetPassword] randomPassword : {}", randomPassword);
    // 문자로 전송
    smsClient.sendPasswordResetCode(request.getPhone(), randomPassword);
    // 랜덤 문자열로 비밀번호 저장
    user.setPassword(passwordEncoder.encode(randomPassword));
  }

  /*
  프로필 사진 업데이트
   */
  private void updateUserProfileImg(User user, MultipartFile image) throws IOException {
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

  /*
  랜덤 문자열 생성 (알파벳 + 숫자)
   */
  private String getRandomPassword() {
    return RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
  }

}
