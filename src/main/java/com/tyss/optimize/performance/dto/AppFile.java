package com.tyss.optimize.performance.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "files")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppFile{

	@Transient
	public static final String SEQUENCE_NAME = "FILE";

	@Id
	public String id;
//	@Size(min = 2, message = "name must be minimum 2 characters")
	String name;
	String type;
	String relativePath;
	String actualPath;
	String projectId;
	private boolean folder;
	String sheetName;
	double executionOrder;
	private boolean imported = false;
	private String url;
	private String fileVersion;
	private String modifiedTimestamp;
	private Boolean executable;
	private String fileSize;
}