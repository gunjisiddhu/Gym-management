package com.epam.gymserviceservice.service;

import com.epam.gymserviceservice.dto.LogDTO;
import com.epam.gymserviceservice.dto.NotificationDTO;
import com.epam.gymserviceservice.exception.GymNotificationException;
import com.epam.gymserviceservice.model.Notification;
import com.epam.gymserviceservice.repository.NotificationLogRepository;
import com.epam.gymserviceservice.util.StringConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationLogService {

    private final NotificationLogRepository notificationLogRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderString;

    public void addNewLog(NotificationDTO notificationDTO) throws GymNotificationException {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), "ADD_NEW_REPORT", this.getClass().getName(), notificationDTO.toString());


        if(notificationDTO.getToEmails().isEmpty() || notificationDTO.getSubject()==null || notificationDTO.getBody()==null)
            throw new GymNotificationException("Please provide valid data to send notification");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(notificationDTO.getFromEmail());
        message.setTo(notificationDTO.getToEmails().toArray(new String[0]));
        message.setCc(notificationDTO.getCcEmails().toArray(new String[0]));
        message.setSubject("Gym Report");
        message.setText(notificationDTO.getBody());

        Notification notification = Notification.builder()
                .fromEmail(senderString)
                .toEmails(notificationDTO.getToEmails())
                .ccEmails(notificationDTO.getCcEmails())
                .body(notificationDTO.getBody())
                .sentTime(LocalDateTime.now().toString())
                .build();
        try {
            mailSender.send(message);
            notification.setStatus("Sent successfully");
            notification.setRemarks("Email sent successfully");
        } catch ( MailException ex) {
            notification.setStatus("Failed");
            notification.setRemarks("Failed to send email: " + ex.getMessage());
        }
        notificationLogRepository.save(notification);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), "ADD_NEW_REPORT", this.getClass().getName());
    }

    public List<LogDTO> getLogEntries() {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), "GET_LOG_ENTRIES", this.getClass().getName());
        List<Notification> notificationLog = notificationLogRepository.findAll();
        List<LogDTO> logs = notificationLog.stream().map(notification -> LogDTO.builder()
                .fromEmail(notification.getFromEmail())
                .toEmails(notification.getToEmails())
                .ccEmails(notification.getCcEmails())
                .body(notification.getBody())
                .sentTime(LocalDateTime.now().toString())
                .status("Successful")
                .remarks("No Remarks")
                .build()).toList();
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), "GET_LOG_ENTRIES", this.getClass().getName());
        return logs;
    }

}
