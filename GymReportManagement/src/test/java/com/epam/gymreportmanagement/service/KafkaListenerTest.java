package com.epam.gymreportmanagement.service;


import com.epam.gymreportmanagement.dto.TrainingReportDTO;
import com.epam.gymreportmanagement.kafka.KafkaListenerForReports;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaListenerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private KafkaListenerForReports kafkaListenerForReports;


    @Test
    void testReadNotificationFromBroker() throws JsonProcessingException {
        TrainingReportDTO reportDTO = new TrainingReportDTO(
                "username",
                "firstName",
                "lastName",
                "email@example.com",
                true,
                LocalDate.now(),
                60
        );

        String jsonData = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(reportDTO);
        Mockito.doNothing().when(reportService).addNewReport(any(TrainingReportDTO.class));

        kafkaListenerForReports.readNotificationFromBroker(jsonData);

        verify(reportService).addNewReport(any(TrainingReportDTO.class));
    }
}
