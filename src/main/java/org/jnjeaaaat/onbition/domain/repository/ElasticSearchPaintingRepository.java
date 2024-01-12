package org.jnjeaaaat.onbition.domain.repository;

import org.jnjeaaaat.onbition.domain.entity.ElasticSearchPainting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * ElasticSearch Painting 접근 Repository
 */
@Repository
public interface ElasticSearchPaintingRepository extends
    ElasticsearchRepository<ElasticSearchPainting, Long> {

  // 제목 또는 태그로 검색
  Page<ElasticSearchPainting> findByTitleOrTags(String title, String tag, Pageable pageable);

  // 제목으로 검색 (판매중인 그림 가격 범위 설정 x)
  Page<ElasticSearchPainting> findByTitleAndIsSaleTrue(String title, Pageable pageable);

  // 제목으로 검색 (판매중인 그림 가격 범위 설정)
  Page<ElasticSearchPainting> findByTitleAndPriceBetween(String title, Long minPrice,
      Long maxPrice, Pageable pageable);

}
