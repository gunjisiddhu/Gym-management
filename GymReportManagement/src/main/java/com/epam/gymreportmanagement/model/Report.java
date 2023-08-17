package com.epam.gymreportmanagement.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerStatus;
    private Map<Long, Map<Long, Map<Long , Map<String ,Long>>>> trainingSummaries; //Year -> Month -> Day -> Date -> Duration

}
