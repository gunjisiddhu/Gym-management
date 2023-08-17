package com.epam.gymreportmanagement.service;

import com.epam.gymreportmanagement.dto.ReportDTO;
import com.epam.gymreportmanagement.dto.TrainingReportDTO;
import com.epam.gymreportmanagement.exception.ReportException;
import com.epam.gymreportmanagement.model.Report;
import com.epam.gymreportmanagement.repository.TrainingReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingReportServiceTest {

    @Mock
    private TrainingReportRepository trainingReportRepository;

    @InjectMocks
    private ReportServiceImpl trainingReportService;



    @Test
    void testAddNewReport() {
        TrainingReportDTO trainingReportDTO = new TrainingReportDTO(
                "username",
                "firstName",
                "lastName",
                "email@example.com",
                true,
                LocalDate.now(),
                60
        );

        Report report = new Report(
                trainingReportDTO.getTrainerUsername(),
                trainingReportDTO.getTrainerFirstName(),
                trainingReportDTO.getTrainerLastName(),
                trainingReportDTO.isTrainerStatus(),
                null
        );

        when(trainingReportRepository.findById(trainingReportDTO.getTrainerUsername())).thenReturn(Optional.of(report));
        when(trainingReportRepository.save(any(Report.class))).thenReturn(report);

        trainingReportService.addNewReport(trainingReportDTO);

        ArgumentCaptor<Report> reportCaptor = ArgumentCaptor.forClass(Report.class);
        verify(trainingReportRepository).save(reportCaptor.capture());
        Report capturedReport = reportCaptor.getValue();

        assertNotNull(capturedReport.getTrainingSummaries());
    }

    @Test
    void testAddNewReportNonExistCase() {
        TrainingReportDTO trainingReportDTO = new TrainingReportDTO(
                "username",
                "firstName",
                "lastName",
                "email@example.com",
                true,
                LocalDate.now(),
                60
        );

        Report report = new Report(
                trainingReportDTO.getTrainerUsername(),
                trainingReportDTO.getTrainerFirstName(),
                trainingReportDTO.getTrainerLastName(),
                trainingReportDTO.isTrainerStatus(),
                null
        );

        when(trainingReportRepository.findById(trainingReportDTO.getTrainerUsername())).thenReturn(Optional.empty());
        when(trainingReportRepository.save(any(Report.class))).thenReturn(report);

        trainingReportService.addNewReport(trainingReportDTO);

        ArgumentCaptor<Report> reportCaptor = ArgumentCaptor.forClass(Report.class);
        verify(trainingReportRepository).save(reportCaptor.capture());
        Report capturedReport = reportCaptor.getValue();

        assertNotNull(capturedReport.getTrainingSummaries());
    }

    @Test
    void testGetReport() throws ReportException {
        Report report = new Report(
                "username",
                "firstName",
                "lastName",
                true,
                new HashMap<>() // Training summaries can be populated based on your test case
        );

        when(trainingReportRepository.findById("username")).thenReturn(java.util.Optional.of(report));

        ReportDTO reportDTO = trainingReportService.getReport("username");

        assertNotNull(reportDTO);
    }

    @Test
    void testGetReportThrowsException() {
        when(trainingReportRepository.findById("nonexistentUsername")).thenReturn(java.util.Optional.empty());

        assertThrows(ReportException.class, () -> trainingReportService.getReport("nonexistentUsername"));
    }
}
