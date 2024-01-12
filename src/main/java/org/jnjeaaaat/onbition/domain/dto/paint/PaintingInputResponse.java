package org.jnjeaaaat.onbition.domain.dto.paint;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.dto.user.SimpleUserDto;
import org.jnjeaaaat.onbition.domain.entity.Painting;

/**
 * 그림 등록 Response class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaintingInputResponse {

  private Long paintingId;    // 그림 PK
  private SimpleUserDto user;   // 그림 소유자
  private String paintingImgUrl;    // 그림 url
  private String title;   // 그림 제목
  private String description;  // 그림 설명
  private Boolean isSale;     // 그림 판매 여부
  private Long auctionPrice;    // 경매 시작 가격
  private Long salePrice;   // 매매 가격
  private Set<String> tags;    // tags 리스트
  private LocalDateTime createdAt;    // 그림 생성 시각
  private LocalDateTime updatedAt;

  public static PaintingInputResponse from(Painting painting) {
    return PaintingInputResponse.builder()
        .paintingId(painting.getId())
        .user(SimpleUserDto.from(painting.getUser()))
        .paintingImgUrl(painting.getPaintingImgUrl())
        .title(painting.getTitle())
        .description(painting.getDescription())
        .isSale(painting.getIsSale())
        .auctionPrice(painting.getAuctionPrice())
        .salePrice(painting.getSalePrice())
        .tags(painting.getTags())
        .createdAt(painting.getCreatedAt())
        .updatedAt(painting.getUpdatedAt())
        .build();
  }

}
