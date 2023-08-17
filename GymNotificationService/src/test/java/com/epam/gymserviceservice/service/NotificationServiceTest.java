package com.epam.gymserviceservice.service;

import com.epam.gymserviceservice.dto.LogDTO;
import com.epam.gymserviceservice.dto.NotificationDTO;
import com.epam.gymserviceservice.exception.GymNotificationException;
import com.epam.gymserviceservice.model.Notification;
import com.epam.gymserviceservice.repository.NotificationLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationLogRepository notificationLogRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationLogService notificationLogService;

    @Test
    void testAddNewLogSuccess()  {


        NotificationDTO notificationDTO = new NotificationDTO("sender@example.com",
                List.of("recipient@example.com"), Collections.emptyList(),
                "Subject", "Body");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(notificationDTO.getFromEmail());
        mailMessage.setTo(notificationDTO.getToEmails().toArray(new String[0]));
        mailMessage.setCc(notificationDTO.getCcEmails().toArray(new String[0]));
        mailMessage.setSubject("Gym Report");
        mailMessage.setText(notificationDTO.getBody());

        Mockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        Mockito.when(notificationLogRepository.save(any(Notification.class))).thenReturn(new Notification());

        assertDoesNotThrow(() -> notificationLogService.addNewLog(notificationDTO));

        Mockito.verify(notificationLogRepository).save(any(Notification.class));
        Mockito.verify(mailSender).send(mailMessage);
        Mockito.verify(notificationLogRepository).save(any(Notification.class));
    }

    @Test
    void testAddNewLogMailException()  {


        NotificationDTO notificationDTO = new NotificationDTO("sender@example.com",
                List.of("recipient@example.com"), Collections.emptyList(),
                "Subject", "Body");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(notificationDTO.getFromEmail());
        mailMessage.setTo(notificationDTO.getToEmails().toArray(new String[0]));
        mailMessage.setCc(notificationDTO.getCcEmails().toArray(new String[0]));
        mailMessage.setSubject("Gym Report");
        mailMessage.setText(notificationDTO.getBody());

        Mockito.doThrow(new MailException("Error") {}).when(mailSender).send(any(SimpleMailMessage.class));
        Mockito.when(notificationLogRepository.save(any(Notification.class))).thenReturn(new Notification());

        assertDoesNotThrow(() -> notificationLogService.addNewLog(notificationDTO));

        verify(mailSender).send(mailMessage);
        verify(notificationLogRepository).save(any(Notification.class));
    }

     @Test
    void testAddNewLogInvalidData() {


        NotificationDTO notificationDTO = new NotificationDTO("", Collections.emptyList(),
                Collections.emptyList(), "", "");

        assertThrows(GymNotificationException.class, () -> notificationLogService.addNewLog(notificationDTO));

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
        verify(notificationLogRepository, never()).save(any(Notification.class));
    }

     @Test
    void testGetLogEntries() {

        List<Notification> notificationLog = Arrays.asList(
                new Notification("1","sender@example.com", Collections.emptyList(), Collections.emptyList(),
                        "Body1", "11-11-2001", "Sent successfully", "No Remarks"),
                new Notification("2", "sender@example.com", Collections.emptyList(), Collections.emptyList(),
                        "body2", "11-11-2001",  "Sent successfully", "No Remarks")
        );

        Mockito.when(notificationLogRepository.findAll()).thenReturn(notificationLog);

        List<LogDTO> logEntries = notificationLogService.getLogEntries();

        assertEquals(2, logEntries.size());
        assertEquals("sender@example.com", logEntries.get(0).getFromEmail());
        assertEquals("Body1", logEntries.get(0).getBody());
        assertEquals("sender@example.com", logEntries.get(1).getFromEmail());
        assertEquals("body2", logEntries.get(1).getBody());

        verify(notificationLogRepository).findAll();
    }
}