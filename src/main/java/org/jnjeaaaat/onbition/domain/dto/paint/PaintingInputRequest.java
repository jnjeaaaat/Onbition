package org.jnjeaaaat.onbition.domain.dto.paint;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.config.annotation.valid.TagSize;

/**
 * 그림 등록 Request class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaintingInputRequest {

  @NotBlank(message = "제목을 입력해주세요")
  private String title;

  @Size(min = 10, max = 300)
  private String description;  // 그림 설명

  @NotNull
  private Boolean isSale;     // 그림 판매 여부

  private Long auctionPrice;    // 경매 시작 가격
  private Long salePrice;   // 매매 가격

  @TagSize
  private List<String> tags;    // tags 리스트

}
