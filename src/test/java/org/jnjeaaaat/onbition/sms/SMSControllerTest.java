package org.jnjeaaaat.onbition.sms;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jnjeaaaat.onbition.config.WithCustomMockUser;
import org.jnjeaaaat.onbition.domain.dto.auth.SendTextResponse;
import org.jnjeaaaat.onbition.service.impl.auth.SMSServiceImpl;
import org.jnjeaaaat.onbition.config.security.JwtTokenProvider;
import org.jnjeaaaat.onbition.web.SMSController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WebMvcTest(SMSController.class)
class SMSControllerTest {

  @MockBean
  SMSServiceImpl smsService;

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  @WithCustomMockUser
  @DisplayName("[controller] 인증코드 전송")
  void send_message() throws Exception {
    //given
    given(smsService.sendMessage(anyString()))
        .willReturn(new SendTextResponse("000000"));

    //when
    //then
    mockMvc.perform(post("/api/v1/sms?phone=010-1234-1234")
            .with(csrf()))

        .andDo(print())
        .andExpect(jsonPath("$.result.verificationCode").value("000000"))
        .andExpect(status().isOk());

  }

  @Test
  @WithCustomMockUser
  @DisplayName("[controller] 인증코드 인증")
  void authenticate_verificationCode() throws Exception {
    //given
    given(smsService.authenticatePhoneNum(anyString(), anyString()))
        .willReturn("010-1234-1234");

    //when
    //then
    mockMvc.perform(post("/api/v1/sms/authentication?phone=010-1234-1234&code=000000")
            .with(csrf()))

        .andExpect(jsonPath("$.result").value("010-1234-1234"))
        .andExpect(status().isOk())
        .andDo(print());
  }


}
