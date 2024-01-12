package org.jnjeaaaat.onbition.web;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_CONVERT_TO_SALE;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_CREATE_PAINTING;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_MODIFY_PAINTING_TAGS;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_SEARCH_PAINTING;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_SEARCH_PAINTINGS_BY_TITLE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.annotation.AuthUser;
import org.jnjeaaaat.onbition.domain.dto.base.BaseResponse;
import org.jnjeaaaat.onbition.domain.dto.page.CustomPageRequest;
import org.jnjeaaaat.onbition.domain.dto.page.OnlySalePageDto;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputResponse;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingModifyPriceRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingModifyTagsRequest;
import org.jnjeaaaat.onbition.domain.entity.ElasticSearchPainting;
import org.jnjeaaaat.onbition.service.PaintingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 그림 등록, 그림 수정, 그림 조회 api
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paintings")
public class PaintingController {

  private final PaintingService paintingService;

  /*
  [그림 등록]
  Request: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트
  Response: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트, 등록한 시간, 변경 시간
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

  /*
  [그림 하나 조회]
  Request: user id, painting PK
  Response: ElasticSearchPainting
   */
  @GetMapping("/{paintingId}")
  public BaseResponse<ElasticSearchPainting> getPainting(
      @AuthUser UserDetails userDetails,
      @PathVariable Long paintingId) {

    log.info("[getPainting] 그림 하나 조회 요청");

    return BaseResponse.success(
        SUCCESS_SEARCH_PAINTING,
        paintingService.getPainting(userDetails.getUsername(), paintingId)
    );
  }

  /*
  [그림 제목으로 검색]
  Request: keyword, paging option(sorting field, sorting direction), 판매여부, 최소가격, 최대가격
  Response: ElasticSearchPainting Slice 로 반환
   */
  @GetMapping("/search")
  public BaseResponse<Slice<ElasticSearchPainting>> searchPaintings(
      @RequestParam(required = false) String keyword,  // keyword가 없을때 전체그림 조회
      @Valid @RequestBody CustomPageRequest customPageRequest) {

    // paging을 위한 page num, page size, sort option 설정
    Pageable pageable = PageRequest.of(customPageRequest.getPage(), customPageRequest.getSize(),
        customPageRequest.getSortOption());

    // 판매중인 그림에 대한 data 저장
    OnlySalePageDto onlySalePageDto = OnlySalePageDto.of(customPageRequest);

    return BaseResponse.success(
        SUCCESS_SEARCH_PAINTINGS_BY_TITLE,
        paintingService.searchPaintings(keyword, pageable, onlySalePageDto)
    );
  }

  /*
  [그림 태그 변경]
  Request: user id, painting PK, Set tag
  Response: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트, 등록한 시간, 변경 시간
   */
  @PutMapping("/tags/{paintingId}")
  public BaseResponse<PaintingInputResponse> updatePaintingTags(
      @AuthUser UserDetails userDetails,
      @Positive @PathVariable("paintingId") Long paintingId,
      @RequestBody PaintingModifyTagsRequest request) {

    log.info("[updatePaintingTags] 그림 태그 수정");

    return BaseResponse.success(
        SUCCESS_MODIFY_PAINTING_TAGS,
        paintingService.updatePaintingTags(userDetails.getUsername(), paintingId, request)
    );

  }

  /*
  [그림 판매 시작]
  Request: user id, painting PK, auctionPrice, salePrice
  Response: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트, 등록한 시간, 변경 시간
   */
  @PutMapping("/{paintingId}")
  public BaseResponse<PaintingInputResponse> convertPaintingToSale(
      @AuthUser UserDetails userDetails,
      @Positive @PathVariable("paintingId") Long paintingId,
      @RequestBody PaintingModifyPriceRequest request) {

    log.info("[changePaintingToSale] 그림 등록 요청");

    return BaseResponse.success(
        SUCCESS_CONVERT_TO_SALE,
        paintingService.convertPaintingToSale(userDetails.getUsername(), paintingId, request)
    );

  }



}
