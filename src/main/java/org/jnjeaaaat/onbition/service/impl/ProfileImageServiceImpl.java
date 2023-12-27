package org.jnjeaaaat.onbition.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.jnjeaaaat.onbition.service.FileService;
import org.jnjeaaaat.onbition.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image 저장, 삭제, 조회하는 service interface 구현체
 * User profileImg 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ImageService {
  private final FileService fileService;

  /*
  [이미지 저장]
  MultipartFile 타입의 image, PROFILE_IMAGE
   */
  @Override
  public String saveImage(MultipartFile image, FileFolder fileFolder) {
    return fileService.uploadFile(image, fileFolder);
  }

  /*
  [이미지 삭제]
  file 경로값을 받아 해당 이미지(파일) 삭제
   */
  @Override
  public void deleteImage(String filePath) {
    fileService.deleteFile(filePath);
  }

}
