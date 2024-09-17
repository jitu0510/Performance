package com.tyss.optimize.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvDataSet {

	private boolean testData;
	private boolean localPath;
	private String filePath;
}
