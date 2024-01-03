package org.jnjeaaaat.onbition.service.impl.paint;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UNDER_MIN_PRICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.jnjeaaaat.onbition.domain.entity.Painting;
import org.jnjeaaaat.onbition.domain.entity.User;
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
}