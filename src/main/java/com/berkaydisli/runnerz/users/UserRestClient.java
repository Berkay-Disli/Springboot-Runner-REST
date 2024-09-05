package com.berkaydisli.runnerz.users;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class UserRestClient {
    private final RestClient restClient;

    public UserRestClient(RestClient.Builder builder) {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(5000);
        this.restClient = builder
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .requestFactory(requestFactory)
                .build();
    }

    public List<User> findAll() {
        return restClient
                .get()
                .uri("users")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public User findById(Integer id) {
        return restClient
                .get()
                .uri("users/{id}", id)
                .retrieve()
                .body(User.class);
    }
}