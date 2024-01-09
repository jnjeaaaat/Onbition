package org.jnjeaaaat.onbition.domain.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.user.SimpleUserDto;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

/**
 * ElasticSearch Painting Document class
 */
@Document(indexName = "paintings", writeTypeHint = WriteTypeHint.FALSE)
@Setting(settingPath = "/elasticsearch/painting-setting.json")
@Mapping(mappingPath = "/elasticsearch/painting-mapping.json")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ElasticSearchPainting {

  @Id
  private Long id;
  private SimpleUserDto user;

  @Field(type = FieldType.Text, name = "paintingImgUrl")
  private String paintingImgUrl;    // 그림 url

  @Field(type = FieldType.Keyword, name = "title")
  private String title;   // 그림 제목

  @Field(type = FieldType.Keyword, name = "description")
  private String description;  // 그림 설명

  @Field(type = FieldType.Boolean, name = "isSale")
  private Boolean isSale;     // 그림 판매 여부

  @Field(type = FieldType.Long, name = "auctionPrice")
  private Long auctionPrice;    // 경매 시작 가격

  @Field(type = FieldType.Long, name = "salePrice")
  private Long salePrice;   // 매매 가격

  @Field(type = FieldType.Text, name = "tags")
  private Set<String> tags;  // 그림 태그

  @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
  private LocalDateTime createdAt;
  @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
  private LocalDateTime updatedAt;

  public static ElasticSearchPainting from(Painting painting) {
    return ElasticSearchPainting.builder()
        .id(painting.getId())
        .user(SimpleUserDto.from(painting.getUser()))
        .paintingImgUrl(painting.getPaintingImgUrl())
        .title(painting.getTitle())
        .description(painting.getDescription())
        .isSale(painting.isSale())
        .auctionPrice(painting.getAuctionPrice())
        .salePrice(painting.getSalePrice())
        .tags(painting.getTags())
        .createdAt(painting.getCreatedAt())
        .updatedAt(painting.getUpdatedAt())
        .build();

  }

}
