package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scripts {

    private PreProcessor preProcessor;
    private PostProcessor postProcessor;
}
