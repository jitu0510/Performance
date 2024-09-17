package com.tyss.optimize.performance.dto;

import java.util.List;

import com.tyss.optimize.performance.dto.controller.Controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreadGroup {

    private String threadGroupId;
    private String threadGroupName;
    private CsvDataSet csvDataSet;
    private ThreadGroupProperties threadGroupProperties;
    private ThreadGroupNormalProperties normalProperties;
    private List<ThreadGroupRampAndHoldProperties> threadGroupRampAndHoldPropertiesList;
    private List<Controller> controller;

}
