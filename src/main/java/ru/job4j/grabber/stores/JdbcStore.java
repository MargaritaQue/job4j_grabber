package ru.job4j.grabber.stores;

import ru.job4j.grabber.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {
    private final Connection connection;

    public JdbcStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO posts (name, text, link, created) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, post.getLink());
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(post.getTime()));
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM post;");
            ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new Post(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("link"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("created").getTime()
                    );
                    posts.add(post);
                }
        } catch (Exception e) {
                e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Optional<Post> findById(Long id) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Post post = new Post(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("link"),
                            rs.getString("text"),
                            rs.getTimestamp("created").getTime()
                    );
                    return Optional.of(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}