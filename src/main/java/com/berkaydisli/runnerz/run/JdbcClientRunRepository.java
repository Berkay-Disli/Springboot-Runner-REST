package com.berkaydisli.runnerz.run;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class JdbcClientRunRepository {

    private List<Run> runs = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(JdbcClientRunRepository.class.getName());
    private final JdbcClient jdbcClient;

    public JdbcClientRunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    /* For local testing
    public  List<Run> findAll() {
        return runs;
    }

    public Optional<Run> findById(Integer id) {
        return  runs.stream()
                .filter(run -> run.id() == id)
                .findFirst();
    }

    void create(Run run) {
        runs.add(run);
    }

    void update(Run run, Integer id) {
        Optional<Run> existingRun = findById(id);
        if (existingRun.isPresent()) {
           runs.set(runs.indexOf(existingRun.get()), run);
        }
    }

    void delete(Integer id) {
        runs.removeIf(run -> run.id().equals(id));
    }

    void deleteMultiple(List<Integer> ids) {
        runs.removeIf(run -> ids.contains(run.id()));
    }

    @PostConstruct
    private void init() {
        runs.add(new Run(1, "Run 1", LocalDateTime.now(), LocalDateTime.now(). plusHours(2), 3, Location.OUTDOOR));
        runs.add(new Run(2, "Run 2", LocalDateTime.now(), LocalDateTime.now(). plusHours(1), 2, Location.INDOOR));
    }
    */

    public List<Run> findAll() {
        return jdbcClient.sql("SELECT * FROM Run")
                .query(Run.class)
                .list();
    }

    public Optional<Run> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM Run WHERE id = :id")
                .param("id", id)
                .query(Run.class)
                .optional();
    }

    public void create(Run run) {
        jdbcClient.sql("INSERT INTO Run (id, title, started_on, completed_on, miles, location) VALUES (?, ?, ?, ?, ?, ?)")
                .params(List.of(run.id(), run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString()))
                .update();
    }

    public void update(Run run, Integer id) {
        jdbcClient.sql("UPDATE Run SET title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? WHERE id = ?")
                .params(List.of(run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString(), id))
                .update();
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("DELETE FROM Run WHERE id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Run not found with id " + id);
    }

    public void deleteMultiple(List<Integer> ids) {
        var updated = jdbcClient.sql("DELETE FROM Run WHERE id IN (:ids)")
                .param("ids", ids)
                .update();

        Assert.state(updated == ids.size(), "Run not found with ids " + ids);
    }

    public int count() {
        return jdbcClient.sql("SELECT * FROM Run")
                .query()
                .listOfRows().size();
    }

    public void saveAll(List<Run> runs) {
        runs.forEach(this::create);
    }

    public List<Run> findByLocation(String location) {
        return jdbcClient.sql("SELECT * FROM Run WHERE location = :location")
                .param("location", location)
                .query(Run.class)
                .list();
    }
}
