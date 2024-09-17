package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonExtractor {

    private String variableName;
    private String jsonPathExpression;
    private String defaultValue;
}
