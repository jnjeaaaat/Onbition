package org.jnjeaaaat.onbition.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jnjeaaaat.onbition.common.MockImage;
import org.jnjeaaaat.onbition.config.WithCustomMockUser;
import org.jnjeaaaat.onbition.config.security.JwtTokenProvider;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputResponse;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingModifyPriceRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingModifyTagsRequest;
import org.jnjeaaaat.onbition.domain.dto.user.SimpleUserDto;
import org.jnjeaaaat.onbition.domain.entity.ElasticSearchPainting;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.service.impl.paint.PaintingServiceImpl;
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
@WebMvcTest(PaintingController.class)
class PaintingControllerTest {

  @MockBean
  private PaintingServiceImpl paintingService;

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
  @DisplayName("[controller] 그림 등록 성공")
  void success_create_painting() throws Exception {
    //given
    Set<String> tagSet = Set.of("tag1", "tag2", "tag3");
    given(paintingService.createPainting(anyString(), any(), any()))
        .willReturn(PaintingInputResponse.builder()
            .paintingId(1L)
            .title("testTitle")
            .description("testDescription")
            .tags(tagSet)
            .build());

    // request builder
    PaintingInputRequest request =
        PaintingInputRequest.builder()
            .title("testTitle")
            .description("testDescription")
            .tags(tagSet)
            .isSale(true)
            .auctionPrice(1000L)
            .salePrice(1000L)
            .build();

    String valueAsString = objectMapper.writeValueAsString(request);

    //when
    //then
    mockMvc.perform(multipart("/api/v1/paintings")
            .file(MockImage.getImageFile())
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

  @Test
  @WithCustomMockUser
  @DisplayName("[controller] 그림 하나 조회")
  void success_search_painting() throws Exception {
    //given=
    given(paintingService.getPainting(anyString(), anyLong()))
        .willReturn(ElasticSearchPainting.builder()
            .id(1L)
            .user(SimpleUserDto.builder()
                .userId(1L)
                .profileImgUrl("testProfileUrl")
                .uid("testUid")
                .name("testName")
                .build())
            .paintingImgUrl("testPaintingUrl")
            .title("testTitle")
            .description("testDescription")
            .isSale(false)
            .auctionPrice(0L)
            .salePrice(0L)
            .tags(Set.of("tag1", "tag2"))
            .build());
    //when
    //then
    mockMvc.perform(get("/api/v1/paintings/1")
            .with(csrf()))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result.title").value("testTitle"))
        .andDo(print());
  }

  @Test
  @WithCustomMockUser
  @DisplayName("[controller] 태그 변경")
  void success_modify_painting_tags() throws Exception {
    //given
    Set<String> tagSet = new HashSet<>(List.of("tag1", "tag2", "tag3"));
    given(paintingService.updatePaintingTags(anyString(), anyLong(), any()))
        .willReturn(PaintingInputResponse.builder()
            .paintingId(1L)
            .title("testTitle")
            .description("testDescription")
            .tags(tagSet)
            .build());

    // request builder
    PaintingModifyTagsRequest request =
        PaintingModifyTagsRequest.builder()
            .tags(tagSet)
            .build();

    String valueAsString = objectMapper.writeValueAsString(request);

    //when
    //then
    mockMvc.perform(put("/api/v1/paintings/tags/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString)
            .with(csrf()))

        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithCustomMockUser
  @DisplayName("[controller] 가격 변경")
  void success_modify_painting_price() throws Exception {
    //given
    given(paintingService.convertPaintingToSale(anyString(), anyLong(), any()))
        .willReturn(PaintingInputResponse.builder()
            .paintingId(1L)
            .title("testTitle")
            .description("testDescription")
            .isSale(true)
            .auctionPrice(10000L)
            .salePrice(10000L)
            .build());

    // request builder
    PaintingModifyPriceRequest request =
        PaintingModifyPriceRequest.builder()
            .auctionPrice(10000L)
            .salePrice(10000L)
            .build();

    String valueAsString = objectMapper.writeValueAsString(request);

    //when
    //then
    mockMvc.perform(put("/api/v1/paintings/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString)
            .with(csrf()))

        .andExpect(status().isOk())
        .andDo(print());
  }


}