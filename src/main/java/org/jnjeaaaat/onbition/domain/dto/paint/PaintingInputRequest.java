package org.jnjeaaaat.onbition.domain.dto.paint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
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

  private Long price;   // 매매 가격

  @TagSize
  private Set<String> tags;    // tags 리스트

}
