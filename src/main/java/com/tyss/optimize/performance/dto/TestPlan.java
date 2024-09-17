package com.tyss.optimize.performance.dto;


import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "optimize_performance_testplan")
public class TestPlan {

    @Id
    private String id;
    private String testPlanName;
    private ConfigElement configElement;
    private LiveMonitoring liveMonitoring;
    private boolean report;
    private String reportPath;

    private List<ThreadGroup> threadGroups;

}
