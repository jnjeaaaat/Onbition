package org.jnjeaaaat.onbition.service.impl.paint;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_PAINTING;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UNDER_MIN_PRICE;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UN_MATCH_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.WRONG_PRICE_RANGE;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.jnjeaaaat.onbition.domain.dto.page.OnlySalePageDto;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputResponse;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingModifyPriceRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingModifyTagsRequest;
import org.jnjeaaaat.onbition.domain.entity.ElasticSearchPainting;
import org.jnjeaaaat.onbition.domain.entity.Painting;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.repository.ElasticSearchPaintingRepository;
import org.jnjeaaaat.onbition.domain.repository.PaintingRepository;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.ImageService;
import org.jnjeaaaat.onbition.service.PaintingService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 그림 등록, 수정, 조회 service interface 구현체 class
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaintingServiceImpl implements PaintingService {

  private final ImageService imageService;

  private final UserRepository userRepository;
  private final PaintingRepository paintingRepository;
  private final ElasticSearchPaintingRepository elasticSearchPaintingRepository;

  private final String DREAMER = "ROLE_DREAMER";

  /*
  [그림 등록]
  Request: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트
  Response: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트, 등록한 시간, 변경 시간
   */
  @Override
  public PaintingInputResponse createPainting(String uid, MultipartFile image,
      PaintingInputRequest request) throws IOException {

    log.info("[createPainting] 그림 등록");

    User user = userRepository.findByUidAndDeletedAtNull(uid)
        .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

    // isSale이 true인데 가격이 1000원 미만일때
    if (request.getIsSale() && (request.getAuctionPrice() < 1000L
        || request.getSalePrice() < 1000L)) {
      throw new BaseException(UNDER_MIN_PRICE);
    }

    // user 권한 "DREAMER" 추가
    user.addRoles(DREAMER);

    // 사진 저장 후 url 가져오기
    String imageUrl = imageService.saveImage(image, FileFolder.PAINT_IMAGE);

    Painting painting = Painting.builder()
        .user(user)
        .paintingImgUrl(imageUrl)
        .title(request.getTitle())
        .description(request.getDescription())
        .isSale(request.getIsSale())
        .auctionPrice(request.getAuctionPrice())
        .salePrice(request.getSalePrice())
        .tags(request.getTags())
        .build();

    Painting savedPainting = paintingRepository.save(painting);

    // ES에 저장
    log.info("[createPainting] ES 저장 시작");
    elasticSearchPaintingRepository.save(ElasticSearchPainting.from(savedPainting));
    log.info("[createPainting] ES 저장 끝");

    return PaintingInputResponse.from(savedPainting);
  }

  /*
  [그림 하나 조회]
  Request: user id, painting PK
  Response: ElasticSearchPainting
   */
  @Override
  public ElasticSearchPainting getPainting(String uid, Long paintingId) {
    log.info("[getPainting] 그림 하나 조회");

    return elasticSearchPaintingRepository.findById(
            paintingId)
        .orElseThrow(() -> new BaseException(NOT_FOUND_PAINTING));
  }

  /*
  [그림 제목으로 검색]
  Request: keyword, paging option(sorting field, sorting direction), 판매여부, 최소가격, 최대가격
  Response: ElasticSearchPainting Slice 로 반환
   */
  @Override
  public Slice<ElasticSearchPainting> searchPaintings(String keyword, Pageable pageable,
      OnlySalePageDto onlySalePageDto) {

    log.info("[searchPaintings] 그림 검색 keyword : {}", keyword);
    // 전체 그림 조회
    if (keyword == null) {
      log.info("[searchPaintings] 전체 그림 조회");
      return elasticSearchPaintingRepository.findAll(pageable);
    }

    // 검색된 전체 그림을 조회할때
    if (!onlySalePageDto.getOnlySale()) {
      log.info("[searchPaintings] 판매중이 아닌 그림 조회");
      return elasticSearchPaintingRepository.findByTitleOrTags(keyword, keyword, pageable);
    }

    Long minPrice = onlySalePageDto.getMinPrice();
    Long maxPrice = onlySalePageDto.getMaxPrice();

    log.info("[searchPaintings] 판매중인 그림 min : {}, max : {}", minPrice, maxPrice);
    // 가격 범위를 지정하지 않았을 때
    if (maxPrice == 0) {
      return elasticSearchPaintingRepository.findByTitleAndIsSaleTrue(keyword, pageable);
    }

    // 최대가격이 최소가격보다 작을때
    if (maxPrice < minPrice) {
      throw new BaseException(WRONG_PRICE_RANGE);
    }

    // 판매중이고 가격 범위를 지정한 그림을 조회할때
    return elasticSearchPaintingRepository.findByTitleAndSalePriceBetween(
        keyword, minPrice, maxPrice, pageable
    );

  }

  /*
  [그림 태그 변경]
  Request: user id, painting PK, Set tag
  Response: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트, 등록한 시간, 변경 시간
   */
  @Override
  @Transactional
  public PaintingInputResponse updatePaintingTags(String uid, Long paintingId,
      PaintingModifyTagsRequest request) {
    log.info("[updatePaintingTags] 그림 정보 수정");
    Painting painting = paintingRepository.findById(paintingId)
        .orElseThrow(() -> new BaseException(NOT_FOUND_PAINTING));

    // 그림의 소유주와 접근 유저가 다를때
    if (!Objects.equals(uid, painting.getUser().getUid())) {
      throw new BaseException(UN_MATCH_USER);
    }

    // 원래 태그랑 다를때만 수정
    if (!painting.getTags().equals(request.getTags())) {
      log.info("[updatePaintingTags] tag 변경");
      updateTag(painting, request.getTags());
    }

    return PaintingInputResponse.from(painting);
  }

  /*
  [그림 판매 시작]
  Request: user id, painting PK, auctionPrice, salePrice
  Response: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트, 등록한 시간, 변경 시간
   */
  @Override
  @Transactional
  public PaintingInputResponse convertPaintingToSale(String uid, Long paintingId,
      PaintingModifyPriceRequest request) {

    log.info("[convertPaintingToSale] 그림 판매 시작");
    Painting painting = paintingRepository.findById(paintingId)
        .orElseThrow(() -> new BaseException(NOT_FOUND_PAINTING));

    // 그림의 소유주와 접근 유저가 다를때
    if (!Objects.equals(uid, painting.getUser().getUid())) {
      throw new BaseException(UN_MATCH_USER);
    }

    // 가격이 1000원 보다 낮을때
    if (request.getAuctionPrice() < 1000L || request.getSalePrice() < 1000L) {
      throw new BaseException(UNDER_MIN_PRICE);
    }

    if (!painting.getIsSale()) {
      log.info("[convertPaintingToSale] 가격 변경");
      updatePrice(painting, request);
    }

    return PaintingInputResponse.from(painting);
  }

  // 그림 태그 변경 method
  private void updateTag(Painting painting, Set<String> tags) {
    log.info("[updateTag] db tag 변경 시작");
    painting.setTags(tags);
    log.info("[updateTag] db tag 변경 완료");

    ElasticSearchPainting esPainting = elasticSearchPaintingRepository.findById(painting.getId())
        .orElseThrow(() -> new BaseException(NOT_FOUND_PAINTING));

    log.info("[updateTag] es tag 변경 시작");
    esPainting.setTags(tags);
    elasticSearchPaintingRepository.save(esPainting);
    log.info("[updateTag] es tag 변경 완료");
  }

  // 그림 판매여부, 가격 변경 method
  private void updatePrice(Painting painting, PaintingModifyPriceRequest request) {
    log.info("[updatePrice] db price 변경 시작");
    painting.setIsSale(true);
    painting.setAuctionPrice(request.getAuctionPrice());
    painting.setSalePrice(request.getSalePrice());
    log.info("[updatePrice] db price 변경 완료");

    ElasticSearchPainting esPainting = elasticSearchPaintingRepository.findById(painting.getId())
        .orElseThrow(() -> new BaseException(NOT_FOUND_PAINTING));

    log.info("[updateTag] es tag 변경 시작");
    esPainting.setIsSale(true);
    esPainting.setAuctionPrice(request.getAuctionPrice());
    esPainting.setSalePrice(request.getSalePrice());
    elasticSearchPaintingRepository.save(esPainting);
    log.info("[updateTag] es tag 변경 완료");

  }

}
