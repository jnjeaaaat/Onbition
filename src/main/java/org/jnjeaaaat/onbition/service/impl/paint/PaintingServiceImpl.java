package org.jnjeaaaat.onbition.service.impl.paint;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UNDER_MIN_PRICE;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputRequest;
import org.jnjeaaaat.onbition.domain.dto.paint.PaintingInputResponse;
import org.jnjeaaaat.onbition.domain.entity.Painting;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.repository.PaintingRepository;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.ImageService;
import org.jnjeaaaat.onbition.service.PaintingService;
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

  private final UserRepository userRepository;
  private final PaintingRepository paintingRepository;
  private final ImageService imageService;

  private final String DREAMER = "ROLE_DREAMER";

  /*
  [그림 등록]
  Request: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트
  Response: 등록하는 유저, 등록하는 그림 이미지, 제목, 설명, 판매여부, 경매가, 매매가, 태그 리스트, 등록한 시간
   */
  @Override
  @Transactional
  public PaintingInputResponse createPainting(String uid, MultipartFile image,
      PaintingInputRequest request) throws IOException {

    log.info("[createPainting] 그림 등록");

    User user = userRepository.findByUidAndDeletedAtNull(uid)
        .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

    // isSale이 true인데 가격이 1000원 미만일때
    if (request.getIsSale() && (request.getAuctionPrice() < 1000L || request.getSalePrice() < 1000L)) {
      throw new BaseException(UNDER_MIN_PRICE);
    }

    // user 권한 "DREAMER" 추가
    user.addRoles(DREAMER);

    // 사진 저장 후 url 가져오기
    String imageUrl = imageService.saveImage(image, FileFolder.PAINT_IMAGE);

    return PaintingInputResponse.from(
        paintingRepository.save(Painting.builder()
            .user(user)
            .paintingImgUrl(imageUrl)
            .title(request.getTitle())
            .description(request.getDescription())
            .isSale(request.getIsSale())
            .auctionPrice(request.getAuctionPrice())
            .salePrice(request.getSalePrice())
            .tags(request.getTags())
            .build())
    );
  }
}
