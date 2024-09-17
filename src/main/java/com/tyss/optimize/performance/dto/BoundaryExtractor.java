package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoundaryExtractor {

    private String variableName;
    private String leftBoundary;
    private String rightBoundary;
    private String defaultValue;

}
