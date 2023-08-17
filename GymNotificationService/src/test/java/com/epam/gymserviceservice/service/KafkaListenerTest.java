package com.epam.gymserviceservice.service;

import com.epam.gymserviceservice.dto.NotificationDTO;
import com.epam.gymserviceservice.exception.GymNotificationException;
import com.epam.gymserviceservice.kafka.KafkaListenerForGym;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class KafkaListenerTest {

    @Mock
    private NotificationLogService notificationLogService;


    @InjectMocks
    private KafkaListenerForGym kafkaListenerForGym;


    @Test
    void testReadNotificationFromBroker() throws JsonProcessingException,GymNotificationException {
        String readData = "{\"fromEmail\": \"sender@example.com\", \"toEmails\": [\"recipient@example.com\"], \"ccEmails\": [], \"subject\": \"Subject\", \"body\": \"Body\"}";

        ArgumentCaptor<NotificationDTO> notificationDTOCaptor = ArgumentCaptor.forClass(NotificationDTO.class);
        doNothing().when(notificationLogService).addNewLog(notificationDTOCaptor.capture());

        kafkaListenerForGym.readNotificationFromBroker(readData);

        verify(notificationLogService, times(1)).addNewLog(any(NotificationDTO.class));

    }
}
