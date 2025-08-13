package ru.job4j.grabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.service.Web;
import ru.job4j.grabber.store.JdbcStore;
import ru.job4j.grabber.store.Store;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        var config = new Config();
        config.load("application.properties");
        Class.forName(config.get("jdbc.driver"));
        var connection = DriverManager.getConnection(config.get("db.url"),
                config.get("db.username"),
                config.get("db.password"));
        Store store = new JdbcStore(connection);

        var scheduler = new SchedulerManager();
        scheduler.init();
        scheduler.load(
                Integer.parseInt(config.get("rabbit.interval")),
                SuperJobGrab.class,
                store
        );

        new Web(store).start(Integer.parseInt(config.get("server.port")));

    }
}
