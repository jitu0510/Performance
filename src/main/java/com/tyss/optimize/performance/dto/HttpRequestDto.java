package com.tyss.optimize.performance.dto;


import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpRequestDto {

	private String id;
    private String httpRequestName;
    private String httpRequestMethod;
    private String baseUrl;
    private String httpRequestTestData;
    private String filePath;
    private Map<String, String> headers;
    
    private CsvDataSet csvDataSet;

    private HttpRequestBody httpRequestBody;
    private Scripts scripts;
    private List<AssertionRequestDto> assertions;
    private Settings settings;

}
