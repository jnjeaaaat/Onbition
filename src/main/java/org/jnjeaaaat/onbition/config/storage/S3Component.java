package org.jnjeaaaat.onbition.config.storage;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * S3 저장할 때 필요한 경로 저장
 * bucket, /user/profile/, /paint/
 */
@Component
@Getter
public class S3Component {

  @Value("${cloud.aws.bucket}")
  private String bucket;

  @Value("${cloud.aws.folder.profile}")
  private String profile;

  @Value("${cloud.aws.folder.paint}")
  private String paint;

}
