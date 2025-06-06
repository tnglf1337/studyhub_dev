package com.studyhub.authentication.adapter.mail;

import com.studyhub.authentication.model.AppUser;
import com.studyhub.authentication.web.EmailChangeRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class MailRequestService {
	public Mono<Void> sendRegistrationConfirmation(AppUser user) {
		String uri = "http://localhost:9083/new-user-registration";

		return WebClient.create()
				.post()
				.uri(uri)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.bodyValue(new RegistrationRequest(user.getUsername(), user.getMail()))
				.retrieve()
				.onStatus(HttpStatusCode::isError, response -> Mono.error(new RuntimeException("Fehlerhafte Antwort: " + response.statusCode())))
				.bodyToMono(String.class)
				.timeout(Duration.ofSeconds(10))
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(10)))  // 3 Retries, dann Exception
				.then();
	}

	public Mono<Void> sendChangeMailInformation(EmailChangeRequest request) {
		String uri = "http://localhost:9083/user-change-mail";

		return WebClient.create()
				.post()
				.uri(uri)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.bodyValue(request)
				.retrieve()
				.onStatus(HttpStatusCode::isError, response -> Mono.error(new RuntimeException("Fehlerhafte Antwort: " + response.statusCode())))
				.bodyToMono(String.class)
				.timeout(Duration.ofSeconds(10))
				.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(10)))  // 3 Retries, dann Exception
				.then();
	}
}
