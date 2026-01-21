package com.mrs.pet_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender mailSender;

  public void sendRequestEmail(
          String to,
          String patientName,
          String doctorName,
          String status,
          LocalDateTime createdAt
  ) {
    if (to == null || to.isEmpty()) {
      log.warn("Patient email is empty, skipping email sending");
      return;
    }

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setTo(to);
      helper.setSubject("New medical request created");
      helper.setText(
              """
              Hello %s,

              A new medical request has been created.

              Doctor: %s
              Status: %s
              Created at: %s

              Best regards,
              Medical Records System
              """.formatted(patientName, doctorName, status, createdAt),
              false
      );

      mailSender.send(message);
      log.info("Email sent successfully to {}", to);

    } catch (Exception e) {
      log.error("Failed to send email to {}", to, e);
    }
  }
}
