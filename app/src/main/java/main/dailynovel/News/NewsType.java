package main.dailynovel.News;

/**
 * Created by bumbk on 10/20/2017.
 */

public class NewsType {
    private String title;
    private String link;
    private String imageURL;

    public NewsType(String title, String link, String imageURL) {
        this.title = title;
        this.link = link;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
