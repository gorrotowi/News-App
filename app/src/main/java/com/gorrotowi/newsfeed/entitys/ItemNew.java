package com.gorrotowi.newsfeed.entitys;

/**
 * Created by Gorro on 29/01/17.
 */

public class ItemNew {

    private String url;
    private String imgUrl;
    private String title;
    private String subTitle;
    private String section;

    public ItemNew(String url, String imgUrl, String title, String subTitle, String section) {
        this.url = url;
        this.imgUrl = imgUrl;
        this.title = title;
        this.subTitle = subTitle;
        this.section = section;
    }

    public String getUrl() {
        return url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getSection() {
        return section;
    }
}
