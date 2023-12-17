package org.jnjeaaaat.onbition.s3;

import static org.assertj.core.api.Assertions.assertThat;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.findify.s3mock.S3Mock;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;

@Import(S3MockConfig.class)
@ActiveProfiles("test")
@SpringBootTest
public class S3StoreTest {

  @Autowired
  private AmazonS3 amazonS3;

  @Autowired
  private static final String BUCKET_NAME = "onbitiontest";

  @BeforeAll
  static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
    s3Mock.start();
    amazonS3.createBucket(BUCKET_NAME);
  }

  @AfterAll
  static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
    amazonS3.shutdown();
    s3Mock.stop();
  }

  @Test
  @DisplayName("AWS s3 저장 성공")
  void success_s3() throws IOException {
    //given
    final String contentType = "application/octet-stream"; //파일타입
    final String filePath = "/Users/parktj/Documents/IntelliJ/personal/Onbition/src/test/goorm.png";
    FileInputStream fileInputStream = new FileInputStream(filePath);

    //Mock파일생성
    MockMultipartFile image = new MockMultipartFile(
        "images", //name
        "goorm.png", //originalFilename
        contentType,
        fileInputStream
    );

    // 파일 변환
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(image.getContentType());

    // 파일 업로드
    PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, filePath,
        new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)), objectMetadata);

    amazonS3.putObject(putObjectRequest);

    //when
    S3Object s3Object = amazonS3.getObject(BUCKET_NAME, filePath);

    //then
    assertThat(s3Object.getObjectMetadata().getContentType()).isEqualTo(contentType);
    assertThat(new String(FileCopyUtils.copyToByteArray(s3Object.getObjectContent()))).isEqualTo("");
  }

}
