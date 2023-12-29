package org.jnjeaaaat.onbition.service;

import java.io.IOException;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image 저장, 삭제, 조회 service interface
 */
public interface ImageService {

  // 이미지 저장
  String saveImage(MultipartFile file, FileFolder fileFolder) throws IOException;

  // 이미지 삭제
  void deleteImage(String filePath);
}
