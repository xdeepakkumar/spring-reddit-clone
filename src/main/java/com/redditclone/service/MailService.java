package com.redditclone.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.redditclone.exception.SpringRedditException;
import com.redditclone.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
	
	
	private final JavaMailSender javaMailSender;
	private final MailContentBuilder mailContentBuilder;
	
	public void sendMail(NotificationEmail notificationEmail) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("abhiguptaxxx@gmail.com");
			messageHelper.setTo(notificationEmail.getRecipient());
			messageHelper.setSubject(notificationEmail.getSubject());
			messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
		};
		try {
			javaMailSender.send(messagePreparator);
			log.info("Activation email sent.");
		} catch (Exception e) {
			throw new SpringRedditException("Exception occured when sending mail to "+ notificationEmail.getRecipient());
		}
	}
}
