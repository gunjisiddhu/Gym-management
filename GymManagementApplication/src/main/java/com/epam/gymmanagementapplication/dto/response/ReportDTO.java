package com.epam.gymmanagementapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    String trainerUsername;
    String trainerFirstName;
    String trainerLastName;
    boolean trainerStatus;
    Map<Long, Map<Long, Map<Long , Map<String ,Long>>>> trainingSummaries;
}
