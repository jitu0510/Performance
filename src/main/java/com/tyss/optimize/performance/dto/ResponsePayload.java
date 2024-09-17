package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePayload {
	
	private String totalDuration;
	private double samplesPerSecond;
	private String maxTime;
	private String minTime;

}
