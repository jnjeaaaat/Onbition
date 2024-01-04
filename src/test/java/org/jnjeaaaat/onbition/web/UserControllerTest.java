package org.jnjeaaaat.onbition.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.jnjeaaaat.onbition.config.WithCustomMockUser;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyRequest;
import org.jnjeaaaat.onbition.domain.dto.user.UserModifyResponse;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.service.impl.user.UserServiceImpl;
import org.jnjeaaaat.onbition.config.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerTest {

  @MockBean
  private UserServiceImpl userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithCustomMockUser
  @DisplayName("[controller] 유저 정보 변경 성공")
  void success_modify_user() throws Exception {
    //given
    final String fileName = "testImage";
    final String contentType = "png"; //파일타입

    //Mock파일생성
    MockMultipartFile image = new MockMultipartFile(
        "multipartFile",
        fileName + "." + contentType,
        contentType,
        "<<data>>".getBytes()
    );

    // request builder
    UserModifyRequest request =
        UserModifyRequest.builder()
            .name("newName")
            .build();

    String valueAsString = objectMapper.writeValueAsString(request);

    given(userService.updateUser(anyLong(), any(), any()))
        .willReturn(UserModifyResponse.builder()
            .uid("testId")
            .name("newName")
            .profileImgUrl("newProfileImgUrl")
            .build());

    //when
    //then
    mockMvc.perform(multipart(HttpMethod.PUT, "/api/v1/user/1")
            .file(image)
            .file(new MockMultipartFile("request", "", "application/json",
                valueAsString.getBytes(StandardCharsets.UTF_8)))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .with(csrf())
            .header("X-AUTH-TOKEN", "testAccessToken"))

        .andExpect(status().isOk())
        .andDo(print());
  }


}