package org.jnjeaaaat.onbition.service.impl.user;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.DUPLICATED_USER_NAME;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NO_AUTHORITY;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SAME_PASSWORD;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UN_MATCH_PASSWORD;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UN_MATCH_PHONE_NUM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.util.Optional;
import org.jnjeaaaat.onbition.common.MockImage;
import org.jnjeaaaat.onbition.domain.dto.user.PasswordModifyRequest;
import org.jnjeaaaat.onbition.domain.dto.user.ResetPasswordRequest;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyRequest;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyResponse;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.repository.TokenRepository;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.impl.ImageServiceImpl;
import org.jnjeaaaat.onbition.util.JwtTokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private TokenRepository tokenRepository;

  @Mock
  private JwtTokenUtil jwtTokenUtil;

  @Mock
  private ImageServiceImpl imageService;

  @InjectMocks
  private UserServiceImpl userService;

  @Spy
  private BCryptPasswordEncoder passwordEncoder;

  @Test
  @DisplayName("[service] 유저 정보 변경 성공")
  void success_update_user() throws IOException {
    //given
    given(userRepository.findById(any()))
        .willReturn(Optional.of(User.builder()
                .id(1L)
                .uid("testId")
                .profileImgUrl("oldProfileImgUrl")
                .name("oldName")
                .build()
            )
        );
    given(jwtTokenUtil.getUserIdFromToken())
        .willReturn(1L);
    given(imageService.saveImage(any(), any()))
        .willReturn("newProfileImgUrl");

    //when
    UserModifyRequest request = UserModifyRequest.builder()
        .name("newName")
        .build();

    UserModifyResponse response = userService.updateUser(
        1L, MockImage.getImageFile(), request);

    //then
    assertEquals("newProfileImgUrl", response.getProfileImgUrl());
    assertEquals("newName", response.getName());
  }

  @Test
  @DisplayName("[service] 유저 정보 변경 실패 - 권한이 없는 토큰")
  void failed_update_user_UN_MATCH_TOKEN() {
    //given
    given(jwtTokenUtil.getUserIdFromToken())
        .willReturn(2L);
    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> userService.updateUser(1L, null, null));
    //then
    assertEquals(NO_AUTHORITY, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 유저 정보 변경 실패 - 없는 유저")
  void failed_update_user_NOT_FOUND_USER() {
    //given
    given(jwtTokenUtil.getUserIdFromToken())
        .willReturn(1L);
    given(userRepository.findById(anyLong()))
        .willReturn(Optional.empty());
    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> userService.updateUser(1L, null, null));
    //then
    assertEquals(NOT_FOUND_USER, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 유저 정보 변경 실패 - 중복된 이름")
  void failed_update_user_DUPLICATED_NAME() {
    //given
    given(jwtTokenUtil.getUserIdFromToken())
        .willReturn(1L);
    given(userRepository.findById(anyLong()))
        .willReturn(Optional.of(User.builder()
            .id(1L)
            .uid("firstUser")
            .name("firstUserName")
            .build()));
    given(userRepository.existsByNameAndDeletedAtNull(anyString()))
        .willReturn(true);

    //when
    UserModifyRequest request = UserModifyRequest.builder()
        .name("secondUserName")
        .build();

    BaseException exception = assertThrows(BaseException.class,
        () -> userService.updateUser(1L, null, request));

    //then
    assertEquals(DUPLICATED_USER_NAME, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 비밀번호 변경 실패 - 비밀번호 다름")
  void failed_update_password_UN_MATCH_PASSWORD() {
    //given
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    given(jwtTokenUtil.getUserIdFromToken())
        .willReturn(1L);
    given(userRepository.findById(anyLong()))
        .willReturn(Optional.of(User.builder()
            .id(1L)
            .uid("firstUser")
            .name("firstUserName")
            .password(passwordEncoder.encode("password"))
            .build()));

    //when
    PasswordModifyRequest request =
        new PasswordModifyRequest("wrongPassword", "newPassword");

    BaseException exception = assertThrows(BaseException.class,
        () -> userService.updatePassword(1L, request));

    //then
    assertEquals(UN_MATCH_PASSWORD, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 비밀번호 변경 실패 - 새 비밀번호와 같음")
  void failed_update_password_SAME_PASSWORD() {
    //given
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    given(jwtTokenUtil.getUserIdFromToken())
        .willReturn(1L);
    given(userRepository.findById(anyLong()))
        .willReturn(Optional.of(User.builder()
            .id(1L)
            .uid("firstUser")
            .name("firstUserName")
            .password(passwordEncoder.encode("originalPassword"))
            .build()));

    //when
    PasswordModifyRequest request =
        new PasswordModifyRequest("originalPassword", "originalPassword");

    BaseException exception = assertThrows(BaseException.class,
        () -> userService.updatePassword(1L, request));

    //then
    assertEquals(SAME_PASSWORD, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 비밀번호 초기화 실패 - 핸드폰 번호 다름")
  void test() {
    //given
    given(userRepository.findByUidAndDeletedAtNull(anyString()))
        .willReturn(Optional.of(User.builder()
            .uid("firstUser")
            .name("firstUserName")
            .phone("010-1111-1111")
            .build()));
    //when
    ResetPasswordRequest request =
        new ResetPasswordRequest("firstUser", "010-2222-2222");
    BaseException exception = assertThrows(BaseException.class,
        () -> userService.resetPassword(request));

    //then
    assertEquals(UN_MATCH_PHONE_NUM, exception.getStatus());
  }

}