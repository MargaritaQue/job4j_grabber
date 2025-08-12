package ru.job4j.grabber.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final Logger LOG = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> fetch(String link) {
        var result = new ArrayList<Post>();
        try {
            var connection = Jsoup.connect(link);
            var document = connection.get();
            var rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                var titleElement = row.select(".vacancy-card__title").first();
                var linkElement = titleElement.child(0);
                var timeElement = row.select(".vacancy-card__date > time").first();

                String vacancyName = titleElement.text();
                String postLink = String.format("%s%s", SOURCE_LINK,
                        linkElement.attr("href"));
                String datetimeStr = timeElement.attr("datetime");
                String description = retrieveDescription(postLink);

                long second = dateTimeParser.parse(datetimeStr).toEpochSecond(ZoneOffset.UTC);

                var post = new Post();
                post.setTitle(vacancyName);
                post.setLink(postLink);
                post.setTime(second);
                post.setDescription(description);
                result.add(post);
                System.out.printf("%s %s %s%n %s%n", vacancyName, postLink, second, description);
            });

        } catch (IOException e) {
            LOG.error("When load page", e);
        }
        return result;
    }

    public List<Post> fetch() {
        List<Post> allPost = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, i, SUFFIX);
            allPost.addAll(fetch(fullLink));
        }
        return allPost;
    }

    private String retrieveDescription(String link) {
        try {
            var connection = Jsoup.connect(link);
            var document = connection.get();
            var descriptionElement = document.select(".faded-content");
            return descriptionElement.text();
        } catch (IOException e) {
            LOG.error("Error retrieving description from: " + link, e);
            return "";
        }
    }
}