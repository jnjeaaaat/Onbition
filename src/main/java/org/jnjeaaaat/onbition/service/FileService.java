package org.jnjeaaaat.onbition.service;

import java.io.FileNotFoundException;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 저장, 삭제, 조회 service interface
 */
public interface FileService {

  // 파일 업로드
  String uploadFile(MultipartFile file, FileFolder fileFolder);

  // 파일 삭제
  void deleteFile(String fileName);

  // 파일 URL 조회
  String getFileUrl(String fileName);

  // 파일 다운로드
  byte[] downloadFile(String fileName) throws FileNotFoundException;

  // 폴더 조회
  String getFileFolder(FileFolder fileFolder);
}
