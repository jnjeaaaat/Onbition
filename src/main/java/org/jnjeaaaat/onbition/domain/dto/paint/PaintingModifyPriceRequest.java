package org.jnjeaaaat.onbition.domain.dto.paint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그림 가격 변경 request class
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaintingModifyPriceRequest {

  private Long auctionPrice;   // 경매가
  private Long salePrice;     // 매매가

}
