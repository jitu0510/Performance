package com.tyss.optimize.performance.dto.controller;

import java.util.List;

import com.tyss.optimize.performance.dto.HttpRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class SimpleController extends Controller{

    private String simpleControllerName;

    private List<HttpRequestDto> httpRequestDto;
}
