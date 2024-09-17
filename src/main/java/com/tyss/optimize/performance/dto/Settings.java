package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

    private boolean redirectsAutomatically;
    private boolean followRedirects;
    private boolean useKeepAlive;

}
