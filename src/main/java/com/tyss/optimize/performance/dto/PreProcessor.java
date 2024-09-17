package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreProcessor {

    private boolean alphabets;
    private boolean numbers;
    private boolean specialCharacters;
    private int length;
    private RandomOutputString randomOutputString;
    private String customLogic;
}
