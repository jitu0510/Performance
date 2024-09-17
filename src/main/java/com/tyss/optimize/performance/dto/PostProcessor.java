package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostProcessor {

    private Set<JsonExtractor> setOfJsonExtractor;
    private Set<BoundaryExtractor> setOfBoundaryExtractor;
    private String customLogic;
}
