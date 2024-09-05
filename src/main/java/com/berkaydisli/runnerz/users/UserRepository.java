package com.berkaydisli.runnerz.users;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    public List<User> users = new ArrayList<>();
    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    public List<User> findAll() {
        return jdbcClient.sql("SELECT * FROM UserTable")
                .query(User.class)
                .list();
    }

    public Optional<User> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM UserTable WHERE id = :id")
                .param("id", id)
                .query(User.class)
                .optional();
    }

    public void saveAll(List<User> users) {
        users.forEach(this::create);
    }

    public void create(User user) {
        jdbcClient.sql("INSERT INTO UserTable (id, name, email) VALUES (?, ?, ?)")
                .params(List.of(user.id(), user.name(), user.email()))
                .update();
    }

    public void update(User user, Integer id) {
        jdbcClient.sql("UPDATE UserTable SET name = ?, email = ? WHERE id = ?")
                .params(List.of(user.name(), user.email(), id))
                .update();
    }

    public void delete(User user) {
        var updated = jdbcClient.sql("DELETE FROM UserTable WHERE id = :id")
                .param("id", user.id())
                .update();

        Assert.state(updated == 1, "User not found with id " + user.id());
    }

    public void deleteAll(List<User> users) {
        users.forEach(this::delete);
    }
}
