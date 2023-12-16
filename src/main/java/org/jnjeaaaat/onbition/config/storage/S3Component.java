package org.jnjeaaaat.onbition.config.storage;

import lombok.Data;
import org.jnjeaaaat.onbition.config.storage.properties.Folder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * S3 저장할 때 필요한 경로 저장 bucket, /user/profile/, /paint/
 */
@Component
@Data
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Component {

  private String bucket;
  private Folder folder;

}
