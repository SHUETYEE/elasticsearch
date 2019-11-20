package com.itheima.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 李靖宇
 * @Project elasticsearch
 * @date 2019/11/19 19:24
 * @commit 生活明朗，万物可爱，人间值得，未来可期
 */
public class TestDemo {

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
    public void test1()  {
        client.admin().indices().prepareCreate("index_hello").get();
        client.close();
    }

    @Test
    public void test2() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .startObject("article")
                        .startObject("properties")
                            .startObject("id")
                                .field("type", "long")
                                .field("store",true)
                            .endObject()
                            .startObject("tittle")
                                .field("type","text")
                                .field("store",true)
                                .field("analyzer","ik_smart")
                            .endObject()
                            .startObject("context")
                                .field("type","text")
                                .field("store",true)
                                .field("analyzer","ik_smart")
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();
        client.admin().indices().preparePutMapping("index_hello")
                .setType("article")
                .setSource(builder)
                .get();
        client.close();
    }

    @Test
    public void test3() throws IOException {
        XContentBuilder builder=XContentFactory.jsonBuilder()
                .startObject()
                    .field("id",1L)
                    .field("title","多地降温幅度多可达10度哈哈哈")
                    .field("content","一架客机在纽约机场被隔离")
                .endObject();
        client.prepareIndex()
                .setIndex("index_hello")
                .setType("article")
                .setId("5")
                .setSource(builder)
                .get();
        client.close();
    }


    @Test
    public void addDocument() throws JsonProcessingException {
        for(int i=0;i<50;i++) {
            Article article = new Article();
            article.setId(3L);
            article.setTitle("我们是一家人");
            article.setContent("输掉游戏后电锯自杀");
            ObjectMapper mapper = new ObjectMapper();
            String valueAsString = mapper.writeValueAsString(article);
            System.out.println(valueAsString);
            client.prepareIndex("index_hello", "article", i+"")
                    .setSource(valueAsString, XContentType.JSON)
                    .get();
        }
        client.close();
    }
}
