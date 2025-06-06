package com.studyhub.actuator.monitoring.track;

import com.studyhub.actuator.DatabaseHealthIndicator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class TrackDatabaseService implements DatabaseHealthIndicator {

	@Value("${app.api.token}")
	private String token;

    @Override
	public Health health() {
		if(trackServiceDatabaseIsHealthy()) return Health.up().build();
		return Health.down().build();
	}

	private boolean trackServiceDatabaseIsHealthy() {
		String uri = "http://localhost:9080/api/get-db-health";
		CompletableFuture<String> health = WebClient.create()
				.get()
				.uri(uri)
				.header("ActuatorAuth", "Bearer " + token)
				.retrieve()
				.bodyToMono(String.class)
				.timeout(Duration.of(3, ChronoUnit.SECONDS))
				.toFuture();

		try {
			String response = health.get();
			return Boolean.parseBoolean(response);

		} catch(ExecutionException | InterruptedException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
