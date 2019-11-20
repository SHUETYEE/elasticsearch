package com.itheima.repositories;

import com.itheima.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author 李靖宇
 * @Project elasticsearch
 * @date 2019/11/20 18:35
 * @commit 生活明朗，万物可爱，人间值得，未来可期
 */
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {

    List<Article> findByTitle(String title);

    List<Article> findByTitleOrContent(String title, String content, Pageable pageable);
}
