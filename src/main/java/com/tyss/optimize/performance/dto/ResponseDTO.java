package com.tyss.optimize.performance.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private int responseCode;
    private Object responseObject;
    private int errorCode;
    private String message;
    private String status;
    private Map<String,Object> responseMap;
//	private Object success;
//    private ErrorResponse errors;
//
//    public boolean hasErrors() {
//        return errors != null;
//    }
//
//    public boolean isSuccess() {
//        return success != null;
//    }

}
