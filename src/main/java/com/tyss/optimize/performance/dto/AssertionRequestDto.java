package com.tyss.optimize.performance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssertionRequestDto {

    private String lhs;
    private String operator;
    private String rhs;//supports int, long, float, boolean and string only for ==, >=, <=, !=, starts, ends, contains
    private String passMessage = "Assertion Successful";
    private String failMessage = "Assertion Failed";
    private String group = "Unassigned";
    private Boolean isEnabled = false;
    private String expression;
    private String name;
    private Boolean maskRhs = false;
}
