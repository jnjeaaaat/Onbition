package org.jnjeaaaat.onbition.service.impl;

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
      // todo: NEED custom exceptionHandler
      throw new IllegalArgumentException(
          String.format("파일 변환 중 에러가 발생하였습니다. (%s)", file.getOriginalFilename()));
    }

    return getFileUrl(fileName);
  }

  // 파일 삭제
  @Override
  public void deleteFile(String fileName) {

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
    if(fileFolder == FileFolder.PROFILE_IMAGE) {
      folder = s3Component.getFolder().getProfile();

    }else if(fileFolder ==FileFolder.PAINT_IMAGE){
      folder = s3Component.getFolder().getPaint();
    }
    return folder;
  }

  private void validateFileExists(String fileName) throws FileNotFoundException {
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
