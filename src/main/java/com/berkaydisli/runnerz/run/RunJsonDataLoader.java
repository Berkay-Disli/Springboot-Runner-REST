package com.berkaydisli.runnerz.run;

import com.berkaydisli.runnerz.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class RunJsonDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RunJsonDataLoader.class.getName());
    private final JdbcClientRunRepository runRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public RunJsonDataLoader(JdbcClientRunRepository runRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.runRepository = runRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (runRepository.count() == 0) {
            logger.info("Loading runs from JSON data");
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/runs.json")) {
                Runs allRuns = objectMapper.readValue(inputStream, Runs.class);
                runRepository.saveAll(allRuns.runs());
            } catch (Exception e) {
                logger.error("Error loading runs from JSON data", e);
            }
        } else {
            logger.info("Runs already loaded");
        }
        /*
        if (userRepository.count() == 0) {
            logger.info("Loading users from JSON data");
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/users.json")) {
                userRepository.saveAll(objectMapper.readValue(inputStream, List.class));
            } catch (Exception e) {
                logger.error("Error loading users from JSON data", e);
            }
        } else {
            logger.info("Users already loaded");
        }*/
    }
}
