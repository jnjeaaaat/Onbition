package org.jnjeaaaat.onbition.service.impl;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.FILE_DELETE_ERROR;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.FILE_UPLOAD_ERROR;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.storage.S3Component;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * AWS S3 bucket 에 파일을 저장하는 file service 구현체 class
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements FileService {

  private final S3Component s3Component;
  private final AmazonS3 amazonS3;

  // 파일 저장
  @Override
  public String uploadFile(MultipartFile file, FileFolder fileFolder) {
    log.info("[uploadFile] 파일 업로드 시도");
    //파일 이름 생성
    String fileName = getFileFolder(fileFolder) + createFileName(file.getOriginalFilename());

    //파일 변환
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());

    //파일 업로드
    try (InputStream inputStream = file.getInputStream()) {
      amazonS3.putObject(
          // S3 bucket 에 파일 업로드
          new PutObjectRequest(s3Component.getBucket(), fileName, inputStream, objectMetadata)
              .withCannedAcl(CannedAccessControlList.PublicReadWrite)
      );
    } catch (IOException e) {
      throw new BaseException(FILE_UPLOAD_ERROR);
    }

    return getFileUrl(fileName);
  }

  // 파일 삭제
  @Override
  public void deleteFile(String filePath) {
    log.info("[deleteFile] S3 파일 삭제 시도");
    String key = getDeleteKey(filePath); // 폴더/파일.확장자
    log.info("[deleteFile] key 값 : {}", key);

    try {

      amazonS3.deleteObject(s3Component.getBucket(), key);

    } catch (AmazonServiceException e) {
      throw new BaseException(FILE_DELETE_ERROR);
    }

    log.info("[deleteFile] S3에 있는 파일 삭제");
  }

  // deleteObject key 생성 (폴더/파일명.확장자)
  private String getDeleteKey(String filePath) {
    String key = filePath.substring(filePath.indexOf(s3Component.getBucket())); // https:// 제거
    return key.substring(key.indexOf("/") + 1); // user/profile/파일명.확장자
  }

  // 파일 url 정보 조회
  @Override
  public String getFileUrl(String fileName) {
    return amazonS3.getUrl(s3Component.getBucket(), fileName).toString();
  }

  // 파일 다운로드
  @Override
  public byte[] downloadFile(String fileName) throws FileNotFoundException {
    return new byte[0];
  }

  //
  @Override
  public String getFileFolder(FileFolder fileFolder) {
    String folder = "";
    if (fileFolder == FileFolder.PROFILE_IMAGE) {
      folder = s3Component.getFolder().getProfile();

    } else if (fileFolder == FileFolder.PAINT_IMAGE) {
      folder = s3Component.getFolder().getPaint();
    }
    return folder;
  }

  private void validateFileExists(String fileName) throws FileNotFoundException {
    log.info("[validateFileExists] 파일 유무 확인");
    if (!amazonS3.doesObjectExist(s3Component.getBucket(), fileName)) {
      throw new FileNotFoundException();
    }
  }

  //파일 이름 생성 로직
  private String createFileName(String originalFileName) {
    return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
  }

  //파일의 확장자명을 가져오는 로직
  private String getFileExtension(String fileName) {
    try {
      return fileName.substring(fileName.lastIndexOf("."));
    } catch (StringIndexOutOfBoundsException e) {
      throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다.", fileName));
    }
  }

  //이미지 URL->파일 이름 변환
  public static String convertToFileName(String imageUrl) {
    String[] path = imageUrl.split("/");
    return path[path.length - 2] + "/" + path[path.length - 1];  //폴더 이름 + 파일 이름
  }
}
