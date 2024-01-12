package org.jnjeaaaat.onbition.domain.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 판매중인 그림 최소가격, 최대가격 설정 dto class
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnlySalePageDto {

  private Boolean onlySale;
  private Long minPrice;
  private Long maxPrice;

  public static OnlySalePageDto of(CustomPageRequest customPageRequest) {
    return OnlySalePageDto.builder()
        .onlySale(customPageRequest.getOnlySale())
        .minPrice(customPageRequest.getMinPrice())
        .maxPrice(customPageRequest.getMaxPrice())
        .build();
  }
}
