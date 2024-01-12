package org.jnjeaaaat.onbition.domain.dto.paint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.entity.Painting;

/**
 * Painting 정보를 간단하게 나타낸 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimplePaintingDto {

  private Long id;
  private String paintingImgUrl;  // 그림 url
  private String title;  // 그림 제목
  private String description;  // 그림 설명

  public static SimplePaintingDto from(Painting painting) {
    return SimplePaintingDto.builder()
        .id(painting.getId())
        .paintingImgUrl(painting.getPaintingImgUrl())
        .title(painting.getTitle())
        .description(painting.getDescription())
        .build();
  }

}
