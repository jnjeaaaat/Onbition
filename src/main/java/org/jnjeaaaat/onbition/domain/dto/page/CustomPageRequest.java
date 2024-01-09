package org.jnjeaaaat.onbition.domain.dto.page;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * Paging 처리를 위한 custom Page Request class
 * page Number, page size, Sort direction (DESC, ASC), sort field 설정
 * 판매중인 그림 여부, 최소가격, 최대가격 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomPageRequest {

  @PositiveOrZero
  private Integer page;
  @Positive
  private Integer size;
  private Sort.Direction direction;
  private String sort;

  private Boolean onlySale;
  private Long minPrice;
  private Long maxPrice;

  // Sort Option
  public Sort getSortOption() {
    // 아무것도 설정하지 않았을때는 생성날짜 내림차순 정렬
    Sort sortOption = Sort.by(Direction.DESC, "createdAt");

    if (direction != null && !sort.isEmpty()) {
      sortOption = Sort.by(direction, sort);
    }

    return sortOption;
  }

}
