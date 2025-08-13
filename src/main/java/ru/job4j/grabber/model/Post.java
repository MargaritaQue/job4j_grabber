package ru.job4j.grabber.model;

import java.util.Objects;

public class Post {

    private long id;
    private String title;
    private String link;
    private String description;
    private long time;

    public Post(long id, String title, String link, String description, long time) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.time = time;
    }

    public Post() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(link, post.link);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(link);
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
