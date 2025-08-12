package ru.job4j.grabber.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import ru.job4j.grabber.model.Post;
import java.io.IOException;
import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final Logger LOG = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";

    @Override
    public List<Post> fetch() {
        var result = new ArrayList<Post>();
        try {
            int pageNumber = 1;
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            var connection = Jsoup.connect(fullLink);
            var document = connection.get();
            var rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                var titleElement = row.select(".vacancy-card__title").first();
                var linkElement = titleElement.child(0);
                var timeElement = row.select(".vacancy-card__date > time");
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK,
                        linkElement.attr("href"));
                String datetimeStr = timeElement.attr("datetime");
                long epochMillis = ZonedDateTime.parse(datetimeStr).toInstant().toEpochMilli();
                var post = new Post();
                post.setTitle(vacancyName);
                post.setLink(link);
                post.setTime(epochMillis);
                result.add(post);
                System.out.printf("%s, %s %s%n", vacancyName, link, epochMillis);
            });
        } catch (IOException e) {
            LOG.error("When load page", e);
        }
        return result;
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse();
        habrCareerParse.fetch();
    }
}