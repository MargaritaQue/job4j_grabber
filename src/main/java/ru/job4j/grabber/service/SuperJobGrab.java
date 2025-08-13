package ru.job4j.grabber.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.store.Store;
import ru.job4j.grabber.util.HabrCareerDateTimeParser;

import java.util.List;

public class SuperJobGrab implements Job {
    private final Parse parse = new HabrCareerParse(new HabrCareerDateTimeParser());

    @Override
    public void execute(JobExecutionContext context) {
        List<Post> posts = parse.fetch();
        var store = (Store) context.getJobDetail().getJobDataMap().get("store");
        posts.forEach(store::save);
    }
}