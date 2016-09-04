package com.alenbeyond.sujin.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by AlenBeyond on 2016/9/3.
 */
@DatabaseTable(tableName = "tb_home")
public class SuJinHome {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "title")
    private String title; // 标题
    @DatabaseField(columnName = "date")
    private String date; // 时间
    @DatabaseField(columnName = "des")
    private String des; // 描述
    @DatabaseField(columnName = "letter")
    private int letter; // 字数
    @DatabaseField(columnName = "view")
    private int view; //查看的人数
    @DatabaseField(columnName = "like")
    private int like; //喜欢的人数
    @DatabaseField(columnName = "image")
    private String image; //图片链接
    @DatabaseField(columnName = "url")
    private String url; //文章地址

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getLetter() {
        return letter;
    }

    public void setLetter(int letter) {
        this.letter = letter;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SuJinHome{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", des='" + des + '\'' +
                ", letter=" + letter +
                ", view=" + view +
                ", like=" + like +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
