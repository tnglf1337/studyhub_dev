package com.studyhub.mail.application.service;

import com.studyhub.mail.adapter.auth.RegistrationRequest;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.util.List;
import java.util.Map;

@Service
public class TemplateMailService {

	private SpringTemplateEngine templateEngine;
	private MailService mailService;

	public TemplateMailService(SpringTemplateEngine templateEngine, MailService mailService) {
		this.templateEngine = templateEngine;
		this.mailService = mailService;
	}

	public void prepareLernNotificationTemplating(Map<String, Integer> futureResult) throws MessagingException {
		Context context = new Context();
		context.setVariable("futureResult", futureResult);

		String text = templateEngine.process("/mail/lern-notification/lern-notification.html", context);

		mailService.sendLernNotification(text, "recipients@gmail.com");
	}

	public void prepareRegistrationConfirmationTemplating(RegistrationRequest  registrationRequest) throws MessagingException {
		Context context = new Context();
		context.setVariable("registrationRequest", registrationRequest);

		String text = templateEngine.process("/mail/registration-confirmation/registration-confirmation.html", context);

		mailService.sendRegistrationConfirmation(text, registrationRequest.mail());
	}

	public void prepareKlausurReminderTemplating(List<String> module, String username, String email) {
		Context context = new Context();
		context.setVariable("module", module);
		context.setVariable("username", username);

		String text = templateEngine.process("/mail/klausur-reminder/klausur-reminder.html", context);

		mailService.sendKlausurReminder(text, email);
	}
}
