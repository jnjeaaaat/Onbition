package org.jnjeaaaat.onbition.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.ALREADY_REGISTERED_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.DUPLICATED_USER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpResponse;
import org.jnjeaaaat.onbition.domain.dto.user.UserDto;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.impl.auth.SignServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ProfileImageServiceImpl imageService;

  @Mock
  private UserDto userDto;

  @InjectMocks
  private SignServiceImpl signService;

  @Spy
  private BCryptPasswordEncoder passwordEncoder;

  @Test
  @DisplayName("[service] 회원가입 성공")
  void success_register() {
    //given
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    MockMultipartFile image = getImageFile();

    // role list
    List<String> roleList = Arrays.asList("ROLE_VIEWER");

    // request
    SignUpRequest request = SignUpRequest.builder()
        .uid("test")
        .password("password")
        .name("testName")
        .phone("010-1234-1234")
        .build();

    given(userRepository.save(any()))
        .willReturn(User.builder()
            .uid("testId")
            .password(passwordEncoder.encode("password")) // 암호화된 비밀번호 저장
            .profileImgUrl("https://onbition/jnjeaaaat/org")
            .name("testName")
            .roles(roleList)
            .phone("010-1234-1334")
            .build());

    //when
    SignUpResponse response = signService.signUp(image, request);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    //then
    assertThat(response.getName()).isEqualTo(request.getName());
    assertEquals("testId", response.getUid());
    assertTrue(passwordEncoder.matches("password", passwordEncoder.encode("password")));

    //verify
    verify(userRepository, times(1)).save(captor.capture());
//    verify(passwordEncoder, times(1)).encode(anyString());
  }

  @Test
  @DisplayName("회원가입 실패 - 중복된 uid")
  void failed_register_duplicated_uid() {
    //given
    /*
    임의의 유저 정보
    uid 값 중복만 확인하는 테스트라서 uid 값만 입력해주면 된다.
     */
    SignUpRequest request = SignUpRequest.builder()
        .uid("testId")
        .build();

    given(userRepository.existsByUidAndDeletedAtNull(anyString()))
        .willReturn(true);

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> signService.signUp(any(MockMultipartFile.class), request));

    //then
    assertEquals(ALREADY_REGISTERED_USER, exception.getStatus());
  }

  @Test
  @DisplayName("회원가입 실패 - 중복된 name")
  void failed_register_duplicated_name() {
    //given
    /*
    임의의 유저 정보
    name 값 중복만 확인하는 테스트라서 name 값만 입력해주면 된다.
     */
    SignUpRequest request = SignUpRequest.builder()
        .name("testName")
        .build();

    given(userRepository.existsByNameAndDeletedAtNull(anyString()))
        .willReturn(true);

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> signService.signUp(any(MockMultipartFile.class), request));

    //then
    assertEquals(DUPLICATED_USER_NAME, exception.getStatus());
  }

  private MockMultipartFile getImageFile() {
    String fileName = "testImage";
    String contentType = "png"; //파일타입

    // Mock파일생성
    return new MockMultipartFile(
        "multipartFile",
        fileName + "." + contentType,
        contentType,
        "<<data>>".getBytes()
    );
  }

}