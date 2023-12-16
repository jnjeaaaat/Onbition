package org.jnjeaaaat.onbition.config.storage.properties;

import lombok.Data;

/**
 * ConfigurationProperties Object Properties mapping 을 위한 Folder 클래스.
 * 1. cloud.aws.folder.profile
 * 2. cloud.aws.folder.paint
 */
@Data
public class Folder {

  private String profile;
  private String paint;

}
