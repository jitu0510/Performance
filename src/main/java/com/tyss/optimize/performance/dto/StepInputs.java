package com.tyss.optimize.performance.dto;

import lombok.Data;

@Data
public class StepInputs {
    private String name;
    private String ifFailed;
    private String passMessage;
    private String failMessage;
    private String stepId;
    private String variableName;
}
