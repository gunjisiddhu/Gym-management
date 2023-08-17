package com.epam.gymserviceservice.kafka;

import com.epam.gymserviceservice.dto.NotificationDTO;
import com.epam.gymserviceservice.exception.GymNotificationException;
import com.epam.gymserviceservice.service.NotificationLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListenerForGym {

    private final NotificationLogService notificationLogService;


    @KafkaListener(topics = "gym-account-notifications", groupId = "notification")
    public void readNotificationFromBroker(String readData) throws JsonProcessingException, GymNotificationException {
        log.info("Received Data from Broker, Data : "+readData);
        NotificationDTO notificationDTO = new ObjectMapper().readValue(readData, NotificationDTO.class);
        notificationLogService.addNewLog(notificationDTO);
        log.info("Exiting Kafka Listener");
    }

}
