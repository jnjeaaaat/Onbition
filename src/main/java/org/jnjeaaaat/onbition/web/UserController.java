package org.jnjeaaaat.onbition.web;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_RESET_PASSWORD;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_UPDATE_PASSWORD;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_UPDATE_USER;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.base.BaseResponse;
import org.jnjeaaaat.onbition.domain.dto.user.PasswordModifyRequest;
import org.jnjeaaaat.onbition.domain.dto.user.ResetPasswordRequest;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyRequest;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyResponse;
import org.jnjeaaaat.onbition.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 유저 정보 변경, 비밀번호 변경, 유저 조회 api
 */
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;

  /*
  [유저 정보 변경]
  Request: 유저 PK, MultipartFile image, 유저 이름
  Response: 유저 PK, 유저 id, 유저 이름, 유저 프로필사진
   */
  @PutMapping(value = "/{userId}", consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public BaseResponse<UserModifyResponse> updateUser(
      @Positive @PathVariable Long userId,
      @RequestPart(value = "image", required = false) MultipartFile image,
      @Valid @RequestPart(value = "request") UserModifyRequest request) {

    log.info("[updateUser] 유저 정보 변경 요청");
    return BaseResponse.success(
        SUCCESS_UPDATE_USER,
        userService.updateUser(userId, image, request)
    );
  }

  /*
  [비밀번호 변경]
  Request: oldPassword, newPassword
  Response: success message
   */
  @PutMapping("/pwd/{userId}")
  public BaseResponse updatePassword(
      @Positive @PathVariable Long userId,
      @Valid @RequestBody PasswordModifyRequest request) {

    log.info("[updatePassword] 유저 비밀번호 변경 요청");
    userService.updatePassword(userId, request);

    return BaseResponse.success(
        SUCCESS_UPDATE_PASSWORD
    );
  }

  /*
  [비밀번호 초기화]
  Request: 유저 id, 유저 핸드폰번호
  Response: success message
   */
  @PutMapping("/pwd/reset")
  public BaseResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

    log.info("[resetPassword] 유저 비밀번호 초기화 요청");
    userService.resetPassword(request);

    return BaseResponse.success(
        SUCCESS_RESET_PASSWORD
    );

  }


}
