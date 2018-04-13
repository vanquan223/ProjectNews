package vn.vnpt.vanquan223.projectnews.model;

public class ListNewsDBModel {
    public int id;
    public String date;
    public String title;
    public String excerpt;
    public String image;
    public String content;

    public ListNewsDBModel(int id, String date, String title, String excerpt, String image, String content) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.excerpt = excerpt;
        this.image = image;
        this.content = content;
    }

    public ListNewsDBModel(/*String date, String title, String excerpt, String image, String content*/) {
        /*this.date = date;
        this.title = title;
        this.excerpt = excerpt;
        this.image = image;
        this.content = content;*/
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
