package com.berkaydisli.runnerz;

import com.berkaydisli.runnerz.users.User;
import com.berkaydisli.runnerz.users.UserHTTPClient;
import com.berkaydisli.runnerz.users.UserRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
public class  Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	UserHTTPClient userHTTPClient() {
		RestClient restClient = RestClient.create("http://jsonplaceholder.typicode.com/");
		HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
		return httpServiceProxyFactory.createClient(UserHTTPClient.class);
	}

//	@Bean
//	CommandLineRunner runner(RunRepository runRepository) {
//		return args -> {
//			Run run = new Run(1, "test", LocalDateTime.now(), LocalDateTime.now(). plusHours(1), 3, Location.OUTDOOR);
//			Run run2 = new Run(2, "test2", LocalDateTime.now(), LocalDateTime.now(). plusHours(2), 4, Location.INDOOR);
//			Run run3 = new Run(3, "test3", LocalDateTime.now(), LocalDateTime.now(). plusHours(3), 5, Location.OUTDOOR);
//
//			runRepository.create(run);
//			runRepository.create(run2);
//			runRepository.create(run3);
//		};
//	}

}
