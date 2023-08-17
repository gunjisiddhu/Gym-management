package com.epam.gymreportmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;


@Builder
@Data
public class ReportDTO {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerStatus;
    private Map<Long, Map<Long, Map<Long , Map<String ,Long>>>> trainingSummaries;
}
