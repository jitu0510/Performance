package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiveMonitoring {

    private boolean viewTree;
    private boolean dashboardVisualization;
    private boolean prometheus;
    private String prometheusUrl;
    private boolean graphana;
    private String graphanaUrl;
    private boolean graphite;
    private String graphiteUrl;
    private boolean influxdb;
    private String influxdbUrl;
    private boolean elasticSearch;
    private String elasticSearchUrl;
    private boolean dataDog;
    private String dataDogUrl;

}
