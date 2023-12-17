package org.jnjeaaaat.onbition;

import org.jnjeaaaat.onbition.config.storage.S3Component;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(S3Component.class)
public class OnbitionApplication {

  public static void main(String[] args) {
    SpringApplication.run(OnbitionApplication.class, args);
  }

}
