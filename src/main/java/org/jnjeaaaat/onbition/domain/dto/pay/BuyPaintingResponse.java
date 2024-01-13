package org.jnjeaaaat.onbition.domain.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.dto.paint.SimplePaintingDto;

/**
 * 그림 구매 Response class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyPaintingResponse {

  private SimplePaintingDto painting;
  private Long currentBalance;

}
