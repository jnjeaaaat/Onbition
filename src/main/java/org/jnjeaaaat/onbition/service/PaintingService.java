package org.jnjeaaaat.onbition.service;

import java.io.IOException;
import org.jnjeaaaat.onbition.domain.dto.page.OnlySalePageDto;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputResponse;
import org.jnjeaaaat.onbition.domain.entity.ElasticSearchPainting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

/**
 * 그림 등록, 수정, 조회 service interface
 */
public interface PaintingService {

  // 새로운 그림 등록
  PaintingInputResponse createPainting(String uid, MultipartFile image, PaintingInputRequest request)
      throws IOException;

  // 그림 하나 조회
  ElasticSearchPainting getPainting(String uid, Long paintingId);

  // 제목으로 검색
  Slice<ElasticSearchPainting> searchPaintings(String keyword, Pageable pageable, OnlySalePageDto onlySalePageDto);


}
