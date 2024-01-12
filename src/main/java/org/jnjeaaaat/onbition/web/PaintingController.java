package org.jnjeaaaat.onbition.web;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_CREATE_PAINTING;

import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.annotation.AuthUser;
import org.jnjeaaaat.onbition.domain.dto.base.BaseResponse;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputResponse;
import org.jnjeaaaat.onbition.service.PaintingService;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 그림 등록, 그림 수정, 그림 조회 api
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paintings")
public class PaintingController {

  private final PaintingService paintingService;

  /*
  [그림 등록]
  Request: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트
  Response: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트, 등록한 시간
   */
  @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public BaseResponse<PaintingInputResponse> createPainting(
      @AuthUser UserDetails userDetails,
      @RequestPart(value = "image", required = false) MultipartFile image,
      @Valid @RequestPart(value = "request") PaintingInputRequest request) throws IOException {

    log.info("[createPaint] 그림 등록 요청");

    return BaseResponse.success(
        SUCCESS_CREATE_PAINTING,
        paintingService.createPainting(userDetails.getUsername(), image, request)
    );

  }



}
