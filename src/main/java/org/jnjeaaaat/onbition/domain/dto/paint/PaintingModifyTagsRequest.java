package org.jnjeaaaat.onbition.domain.dto.paint;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그림 태그 변경 request class
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaintingModifyTagsRequest {

  private Set<String> tags;

}
