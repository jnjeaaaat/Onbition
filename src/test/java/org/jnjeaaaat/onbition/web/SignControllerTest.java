package org.jnjeaaaat.onbition.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpResponse;
import org.jnjeaaaat.onbition.service.impl.SignServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WebMvcTest(SignController.class)
class SignControllerTest {

  @MockBean
  SignServiceImpl signService;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;


  @Test
  @DisplayName("[controller] 회원가입 성공")
  void success_register() throws Exception {
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
    SignUpRequest request =
        SignUpRequest.builder()
            .uid("testId")
            .password("password")
            .name("testName")
            .phone("010-1234-1234")
            .build();

    String valueAsString = objectMapper.writeValueAsString(request);

    List<String> roleList = Arrays.asList("ROLE_VIEWER");
    given(signService.signUp(any(), any()))
        .willReturn(SignUpResponse.builder()
            .profileImgUrl("https://onbition/jnjeaaaat/org")
            .uid("testId")
            .name("testName")
            .roles(roleList)
            .build());

    //when
    //then
    mockMvc.perform(multipart("/api/v1/sign-up")
            .file(image)
            .file(new MockMultipartFile("request", "", "application/json",
                valueAsString.getBytes(StandardCharsets.UTF_8)))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .with(csrf()))

        .andDo(print())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.result.uid").value("testId"))
        .andExpect(jsonPath("$.result.name").value("testName"))
        .andExpect(jsonPath("$.result.roles[0]").value("ROLE_VIEWER"))
        .andExpect(jsonPath("$.result.profileImgUrl").value("https://onbition/jnjeaaaat/org"))
        .andExpect(status().isOk());


  }

}