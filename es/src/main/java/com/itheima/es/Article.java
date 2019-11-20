package com.itheima.es;

/**
 * @author 李靖宇
 * @Project elasticsearch
 * @date 2019/11/19 20:25
 * @commit 生活明朗，万物可爱，人间值得，未来可期
 */
public class Article {
    private long id;
    private String title;
    private String content;

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
