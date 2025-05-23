package com.studyhub.authentication;


import com.studyhub.authentication.db.AppUserRepository;
import com.studyhub.authentication.model.AppUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class AppUserRepositoryTest {

	@Autowired
	AppUserRepository appUserRepository;

	@Test
	@DisplayName("Ein neuer User kann erfolgreich gespeichert werden")
	void test_1() {
		AppUser appUser = new AppUser(null, UUID.randomUUID(), "susi@gmail.com", "susi", "1234", true, true, 1);

		AppUser savedAppUser = appUserRepository.save(appUser);

		assertThat(savedAppUser).isEqualTo(appUser);
	}

	@Test
	@DisplayName("Ein User kann durch seinen username aus der Datenbank geholt werden")
	void test_2() {
		AppUser appUser = new AppUser(null, UUID.randomUUID(), "susi@gmail.com", "susi89", "1234", true, true, 1);
		appUserRepository.save(appUser);

		AppUser foundAppUser = appUserRepository.findByUsername("susi89");

		assertThat(foundAppUser.getUsername()).isEqualTo("susi89");
	}

	@Test
	@DisplayName("Wenn kein User mit einem username in der Datenbank ist, wird null zurückgegeben")
	void test_3() {
		AppUser foundAppUser = appUserRepository.findByUsername("susi89");

		assertThat(foundAppUser).isNull();
	}

	@Test
	@DisplayName("Ein User wird anhand der User-Id aus der Datenbank entfernt")
	void test_4() {
		UUID uuid = UUID.randomUUID();
		AppUser appUser = new AppUser(null, uuid, "susi@gmail.com", "susi89", "1234", true, true, 1);
		appUserRepository.save(appUser);

		appUserRepository.deleteByUserId(uuid);

		AppUser foundAppUser = appUserRepository.findByUsername("susi89");
		assertThat(foundAppUser).isNull();
	}

	@Test
	@DisplayName("notificationSubscription wird erfolgreich geändert von false auf true")
	void test_5() {
		String username = "susi89";
		Boolean activate = true;
		AppUser appUser = new AppUser(null, UUID.randomUUID(), "susi@gmail.com", username, "1234", false, true, 1);
		appUserRepository.save(appUser);

		appUserRepository.updateNotificationSubscription(activate, username);

		AppUser foundAppUser = appUserRepository.findByUsername(username);
		assertThat(foundAppUser.getNotificationSubscription()).isTrue();

	}

	@Test
	@DisplayName("notificationSubscription wird erfolgreich geändert von true auf false")
	void test_6() {
		String username = "susi89";
		Boolean activate = false;
		AppUser appUser = new AppUser(null, UUID.randomUUID(), "susi@gmail.com", username, "1234", true, true, 1);
		appUserRepository.save(appUser);

		appUserRepository.updateNotificationSubscription(activate, username);

		AppUser foundAppUser = appUserRepository.findByUsername(username);
		assertThat(foundAppUser.getNotificationSubscription()).isFalse();
	}

	@Test
	@DisplayName("Der Status für die Notification-Subscription wird erfolgreich zurückgegeben")
	void test_7() {
		AppUser appUser = new AppUser(null, UUID.randomUUID(), "susi@gmail.com", "susi89", "1234", true, true, 1);
		appUserRepository.save(appUser);

		Boolean active = appUserRepository.getNotificationSubscription("susi89");

		assertThat(active).isTrue();
	}

	@Test
	@DisplayName("Das Passwort eines Users wird erfolgreich geändert")
	void test_8() {
		String userId = "f8a72b1c-9d3e-4a5f-8b09-1c2d3e4f5a67";
		String newPassword = "12345";
		AppUser appUser = new AppUser(null, UUID.fromString(userId), "susi@gmail.com", "susi89", "68465465", true, true, 1);
		appUserRepository.save(appUser);

		appUserRepository.updatePassword(newPassword, UUID.fromString(userId));

		AppUser foundAppUser = appUserRepository.findByUsername("susi89");
		assertThat(foundAppUser.getPassword()).isEqualTo(newPassword);
	}

	@Test
	@DisplayName("Das Semester eines Users wird erfolgreich geholt")
	void test_9() {
		AppUser appUser = new AppUser(null, UUID.randomUUID(), "susi@gmail.com", "susi89", "68465465", true, true, 3);
		appUserRepository.save(appUser);

		Integer semester = appUserRepository.findSemesterByUsername("susi89");

		assertThat(semester).isEqualTo(3);
	}

	@Test
	@DisplayName("E-Mail-Adresse eines Users kann erfolgreich geändert werden")
	void test_10() {
		String newMail = "newmail@gmail.com";
		UUID userId = UUID.fromString("f8a72b1c-9d3e-4a5f-8b09-1c2d3e4f5a67");
		AppUser appUser = new AppUser(null, userId, "susi@gmail.com", "susi89", "68465465", true, true, 3);
		appUserRepository.save(appUser);

		appUserRepository.updateMailByUserId(newMail, userId);

		AppUser changedMailAppUser = appUserRepository.findByUsername("susi89");
		assertThat(changedMailAppUser.getMail()).isEqualTo(newMail);
	}

	@Test
	@DisplayName("Ein User kann anhand seiner User-Id gefunden werden")
	void test_11() {
		UUID userId = UUID.fromString("f8a72b1c-9d3e-4a5f-8b09-1c2d3e4f5a67");
		AppUser appUser = new AppUser(null, userId, "susi@gmail.com", "susi89", "68465465", true, true, 3);
		appUserRepository.save(appUser);

		AppUser foundUser = appUserRepository.findByUserId(userId);
		assertThat(foundUser.getUserId()).isEqualTo(userId);
	}
}
