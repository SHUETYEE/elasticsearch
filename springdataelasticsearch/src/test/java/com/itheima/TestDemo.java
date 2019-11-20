package com.itheima;

import com.itheima.entity.Article;
import com.itheima.repositories.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author 李靖宇
 * @Project elasticsearch
 * @date 2019/11/20 18:37
 * @commit 生活明朗，万物可爱，人间值得，未来可期
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestDemo {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public void test1() throws Exception{
        template.createIndex(Article.class);
    }

    @Test
    public void addDocument(){
        for (int i = 0; i < 50; i++) {
            Article article=new Article();
            article.setId(i);
            article.setTitle("项目对象模型"+i);
            article.setContent("编写的项目管理工具");
            articleRepository.save(article);
        }
    }

    @Test
    public void test2(){
        articleRepository.deleteById(1L);
    }
    @Test
    public void updateDocument(){
        Article article=new Article();
        article.setId(2L);
        article.setTitle("项目对象模型的应用");
        article.setContent("编写的项目管理工具的创建");
        articleRepository.save(article);
    }

    @Test
    public void test4(){
        final Iterable<Article> all = articleRepository.findAll();
        final Iterator<Article> iterator = all.iterator();
        while (iterator.hasNext()){
            final Article next = iterator.next();
            System.out.println(next);
        }
    }

    @Test
    public void test5(){
        final Optional<Article> byId = articleRepository.findById(2L);
        final Article article = byId.get();
        System.out.println(article);
    }

    @Test
    public void test6(){
        final List<Article> title = articleRepository.findByTitle("项目");
        for (Article article : title) {
            System.out.println(article);
        }
    }

    @Test
    public void test7(){
        Pageable pageable= PageRequest.of(2,5);
        final List<Article> byTitleOrContent = articleRepository.findByTitleOrContent("模型项目", "工具",pageable);
        for (Article article : byTitleOrContent) {
            System.out.println(article);
        }
    }

    @Test
    public void test8(){
        NativeSearchQuery query=new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery("对象项目").defaultField("title"))
                .withPageable(PageRequest.of(2,5)).build();
        final List<Article> articles = template.queryForList(query, Article.class);
        for (Article article : articles) {
            System.out.println(article);
        }
    }
}
