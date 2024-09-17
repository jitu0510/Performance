package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpRequestBody {

    private String rawData;
    private String rawDataType;

    private Set<FormData> setOfFormData;

}
