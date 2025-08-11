package ru.job4j.model;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(title, post.title) && Objects.equals(link, post.link) && Objects.equals(description, post.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, description);
    }
}
