package com.itheima.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 李靖宇
 * @Project elasticsearch
 * @date 2019/11/19 20:38
 * @commit 生活明朗，万物可爱，人间值得，未来可期
 */
public class SearchIndex {
    private TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "my-elasticsearch").build();
        client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9301));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9302));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9303));

        }

    @Test
    public void test1() {
        QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds("1", "3");
        search(queryBuilder);
    }

    public void search(QueryBuilder queryBuilder ){

        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");

        final SearchResponse searchResponse = client.prepareSearch("index_hello")
                .setTypes("article")
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(5)
                //设置高亮显示
                .highlighter(highlightBuilder)
                .get();
        final SearchHits hits = searchResponse.getHits();
        final Iterator<SearchHit> iterator = hits.iterator();
        System.out.println(iterator.hasNext());
        while (iterator.hasNext()) {
            final SearchHit searchHit = iterator.next();
            System.out.println(searchHit.getSourceAsString());
            System.out.println("------------------------------------");
            final Map<String, Object> source = searchHit.getSource();
            System.out.println(source.get("id"));
            System.out.println(source.get("title"));
            System.out.println(source.get("content"));
        }
        client.close();
    }

    public void search2(QueryBuilder queryBuilder ,String highlightField){

        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("highlightField");
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");

        final SearchResponse searchResponse = client.prepareSearch("index_hello")
                .setTypes("article")
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(5)
                //设置高亮显示
                .highlighter(highlightBuilder)
                .get();
        final SearchHits hits = searchResponse.getHits();
        final Iterator<SearchHit> iterator = hits.iterator();
        System.out.println(iterator.hasNext());
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next();
            final Map<String, HighlightField> fields = searchHit.getHighlightFields();
            final HighlightField highlightField1 = fields.get(highlightField);
            if (highlightField1 != null) {
                final Text[] fragments = highlightField1.getFragments();

                for (Text fragment : fragments) {
                    System.out.println(fragment.toString());
                }
            }
        }
        client.close();
    }


    @Test
    public void test2(){
        QueryBuilder queryBuilder=QueryBuilders.termQuery("title","家");
        search(queryBuilder);
    }

    @Test
    public void test3(){
        QueryBuilder queryBuilder=QueryBuilders.queryStringQuery("家人").defaultField("title");
        search2(queryBuilder,"title");
    }
}
