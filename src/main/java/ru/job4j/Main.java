package ru.job4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.stores.JdbcStore;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ClassNotFoundException {
        var config = new Config();
        config.load("application.properties");
        Class.forName(config.get("db.driver-class-name"));
        try (var connection = DriverManager.getConnection(config.get("db.url"), config.get("db.username"),
                config.get("db.password"));
        var scheduler = new SchedulerManager()) {
            var store = new JdbcStore(connection);
            var post = new Post();
            post.setTitle("Super Java Job");
            store.save(post);
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
            Thread.sleep(10000);
        } catch (SQLException e) {
            LOG.error("When create a connection", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}