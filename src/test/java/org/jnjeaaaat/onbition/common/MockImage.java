package org.jnjeaaaat.onbition.common;

import org.springframework.mock.web.MockMultipartFile;

/**
 * 공통적으로 쓰이는 MockImage
 */
public class MockImage {

  public static MockMultipartFile getImageFile() {
    String fileName = "testImage";
    String contentType = "png"; //파일타입

    // Mock파일생성
    return new MockMultipartFile(
        "multipartFile",
        fileName + "." + contentType,
        contentType,
        "<<data>>".getBytes()
    );
  }

}
