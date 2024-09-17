package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigElement {

    private String localFilePath;
    private Boolean testData;
    private AppFileMinimalDto fileDto;
    private boolean httpCookieManager;
    private boolean httpCacheManager;

}
