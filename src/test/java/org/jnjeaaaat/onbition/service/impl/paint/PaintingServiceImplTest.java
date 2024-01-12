package org.jnjeaaaat.onbition.service.impl.paint;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_PAINTING;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UNDER_MIN_PRICE;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UN_MATCH_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jnjeaaaat.onbition.common.MockImage;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputResponse;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingModifyPriceRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingModifyTagsRequest;
import org.jnjeaaaat.onbition.domain.entity.ElasticSearchPainting;
import org.jnjeaaaat.onbition.domain.entity.Painting;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.repository.ElasticSearchPaintingRepository;
import org.jnjeaaaat.onbition.domain.repository.PaintingRepository;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaintingServiceImplTest {

  @Mock
  private ImageServiceImpl imageService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PaintingRepository paintingRepository;

  @Mock
  private ElasticSearchPaintingRepository elasticSearchPaintingRepository;

  @InjectMocks
  private PaintingServiceImpl paintingService;

  @Test
  @DisplayName("[service] 그림 등록 성공")
  void success_create_painting() throws IOException {
    //given
    Set<String> roleSet = new HashSet<>(List.of("ROLE_VIEWER"));
    User user = User.builder()
        .uid("testId")
        .roles(roleSet)
        .build();

    given(userRepository.findByUidAndDeletedAtNull(anyString()))
        .willReturn(Optional.of(user));

    Set<String> tagSet = Set.of("tag1", "tag2", "tag3");
    PaintingInputRequest request = PaintingInputRequest.builder()
        .title("testTitle")
        .description("testDescription")
        .isSale(false)
        .tags(tagSet)
        .build();

    given(paintingRepository.save(any()))
        .willReturn(Painting.builder()
            .user(user)
            .paintingImgUrl("testPaintingImgUrl")
            .title("testTitle")
            .description("testDescription")
            .isSale(false)
            .auctionPrice(0L)
            .salePrice(0L)
            .tags(tagSet)
            .build());

    given(elasticSearchPaintingRepository.save(any()))
        .willReturn(ElasticSearchPainting.builder()
            .paintingImgUrl("testPaintingImgUrl")
            .title("testTitle")
            .description("testDescription")
            .isSale(false)
            .auctionPrice(0L)
            .salePrice(0L)
            .tags(tagSet)
            .build());

    //when
    PaintingInputResponse response = paintingService.createPainting("testId",
        MockImage.getImageFile(), request);

    //then
    assertEquals("testTitle", response.getTitle());
  }

  @Test
  @DisplayName("[service] 그림 등록 실패 - 최소 가격 미만")
  void failed_create_painting_MIN_PRICE() {
    //given
    given(userRepository.findByUidAndDeletedAtNull(anyString()))
        .willReturn(Optional.of(User.builder()
            .uid("testId")
            .build()));

    //when
    PaintingInputRequest request =
        PaintingInputRequest.builder()
            .isSale(true)
            .auctionPrice(999L)
            .salePrice(1000L)
            .build();

    BaseException exception = assertThrows(BaseException.class,
        () -> paintingService.createPainting("testId", null, request));

    //then
    assertEquals(UNDER_MIN_PRICE, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 그림 하나 조회")
  void success_search_painting() {
    //given
    given(elasticSearchPaintingRepository.findById(anyLong()))
        .willReturn(Optional.of(
            ElasticSearchPainting.builder()
                .id(1L)
                .title("testTitle")
                .description("testDescription")
                .build()));

    //when
    ElasticSearchPainting elasticSearchPainting =
        paintingService.getPainting("testUid", 1L);

    //then
    assertEquals("testTitle", elasticSearchPainting.getTitle());
  }

  @Test
  @DisplayName("[service] 그림 하나 조회 실패 - 해당 그림 없음")
  void failed_search_painting_NOT_FOUND_PAINTING() {
    //given
    given(elasticSearchPaintingRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> paintingService.getPainting("testId", 1L));

    //then
    assertEquals(NOT_FOUND_PAINTING, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 그림 태그 변경 성공")
  void success_update_painting_tags() {
    //given
    Painting painting = Painting.builder()
        .id(1L)
        .user(User.builder()
            .uid("testUid")
            .build())
        .paintingImgUrl("testPaintingImgUrl")
        .title("testTitle")
        .description("testDescription")
        .isSale(false)
        .auctionPrice(0L)
        .salePrice(0L)
        .tags(new HashSet<>(List.of("tag1", "tag2")))
        .build();

    given(paintingRepository.findById(anyLong()))
        .willReturn(Optional.of(painting));

    given(elasticSearchPaintingRepository.findById(anyLong()))
        .willReturn(Optional.of(ElasticSearchPainting.from(painting)));

    Set<String> newTags = new HashSet<>(List.of("tag3", "tag4"));
    PaintingModifyTagsRequest request =
        new PaintingModifyTagsRequest(newTags);

    //when
    PaintingInputResponse response =
        paintingService.updatePaintingTags("testUid", 1L, request);

    //then
    assertEquals(newTags, response.getTags());
  }

  @Test
  @DisplayName("[service] 태그 변경 실패 - 해당 그림 없음")
  void failed_update_painting_tags_NOT_FOUND_PAINTING() {
    //given
    given(paintingRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> paintingService.updatePaintingTags("testUid", 1L, null));

    //then
    assertEquals(NOT_FOUND_PAINTING, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 태그 변경 실패 - 유저 다름")
  void failed_update_painting_tags_UN_MATCH_USER() {
    //given
    given(paintingRepository.findById(anyLong()))
        .willReturn(Optional.of(Painting.builder()
            .id(1L)
            .user(User.builder()
                .uid("testUid")
                .build())
            .build()));

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> paintingService.updatePaintingTags("otherUid", 1L, null));

    //then
    assertEquals(UN_MATCH_USER, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 태그 변경 실패 - 해당 ES 그림 없음")
  void failed_update_painting_tags_NOT_FOUND_ELASTICPAINTING() {
    //given
    given(paintingRepository.findById(anyLong()))
        .willReturn(Optional.of(Painting.builder()
            .id(1L)
            .user(User.builder()
                .uid("testUid")
                .build())
            .tags(new HashSet<>(List.of("tag1", "tag2")))
            .build()));

    given(elasticSearchPaintingRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    Set<String> newTags = new HashSet<>(List.of("tag3", "tag4"));
    PaintingModifyTagsRequest request =
        new PaintingModifyTagsRequest(newTags);

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> paintingService.updatePaintingTags("testUid", 1L, request));

    //then
    assertEquals(NOT_FOUND_PAINTING, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 판매중으로 변경 성공")
  void success_convert_sale() {
    //given
    Painting painting = Painting.builder()
        .id(1L)
        .user(User.builder()
            .uid("testUid")
            .build())
        .paintingImgUrl("testPaintingImgUrl")
        .title("testTitle")
        .description("testDescription")
        .isSale(false)
        .auctionPrice(0L)
        .salePrice(0L)
        .tags(new HashSet<>(List.of("tag1", "tag2")))
        .build();

    given(paintingRepository.findById(anyLong()))
        .willReturn(Optional.of(painting));

    given(elasticSearchPaintingRepository.findById(anyLong()))
        .willReturn(Optional.of(ElasticSearchPainting.from(painting)));

    PaintingModifyPriceRequest request =
        new PaintingModifyPriceRequest(10000L, 12000L);

    //when
    PaintingInputResponse response =
        paintingService.convertPaintingToSale("testUid", 1L, request);

    //then
    assertEquals(true, response.getIsSale());
  }

  @Test
  @DisplayName("[service] 판매중으로 변경 실패 - 해당 그림 없음")
  void failed_convert_sale_NOT_FOUND_PAINTING() {
    //given
    given(paintingRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> paintingService.convertPaintingToSale("testUid", 1L, null));

    //then
    assertEquals(NOT_FOUND_PAINTING, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 판매중으로 변경 실패 - 유저 다름")
  void failed_convert_sale_UN_MATCH_USR() {
    //given
    given(paintingRepository.findById(anyLong()))
        .willReturn(Optional.of(Painting.builder()
            .id(1L)
            .user(User.builder()
                .uid("testUid")
                .build())
            .build()));

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> paintingService.convertPaintingToSale("otherUid", 1L, null));

    //then
    assertEquals(UN_MATCH_USER, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 판매중으로 변경 실패 - 가격 1000원 미만")
  void failed_convert_sale_UNDER_MIN_PRICE() {
    //given
    given(paintingRepository.findById(anyLong()))
        .willReturn(Optional.of(Painting.builder()
            .id(1L)
            .user(User.builder()
                .uid("testUid")
                .build())
            .build()));

    PaintingModifyPriceRequest request =
        new PaintingModifyPriceRequest(900L, 950L);

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> paintingService.convertPaintingToSale("testUid", 1L, request));

    //then
    assertEquals(UNDER_MIN_PRICE, exception.getStatus());
  }

  @Test
  @DisplayName("[service] 판매중으로 변경 실패 - 해당 ES 그림 없음")
  void failed_convert_sale_NOT_FOUND_ELASTICPAINTING() {
    //given
    given(paintingRepository.findById(anyLong()))
        .willReturn(Optional.of(Painting.builder()
            .id(1L)
            .user(User.builder()
                .uid("testUid")
                .build())
            .isSale(false)
            .build()));

    given(elasticSearchPaintingRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    PaintingModifyPriceRequest request =
        new PaintingModifyPriceRequest(10000L, 11000L);

    //when
    BaseException exception = assertThrows(BaseException.class,
        () -> paintingService.convertPaintingToSale("testUid", 1L, request));

    //then
    assertEquals(NOT_FOUND_PAINTING, exception.getStatus());
  }
}