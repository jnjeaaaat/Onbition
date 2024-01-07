package org.jnjeaaaat.onbition.service;

import java.io.IOException;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 그림 등록, 수정, 조회 service interface
 */
public interface PaintingService {

  // 새로운 그림 등록
  PaintingInputResponse createPainting(String uid, MultipartFile image, PaintingInputRequest request)
      throws IOException;

  // 그림 하나 조회
//  PaintingDetailResponse getPainting(String uid, Long paintingId);

}
