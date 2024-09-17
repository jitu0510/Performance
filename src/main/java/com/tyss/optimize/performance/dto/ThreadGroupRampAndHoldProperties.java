package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadGroupRampAndHoldProperties {

    private long users;
    private long holdDuration;
    private long rampDuration;
}
