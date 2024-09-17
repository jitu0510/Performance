package com.tyss.optimize.performance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tyss.optimize.performance.dto.HttpRequestDto;
import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.service.HttpRequestService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/public/performance/http-request")
public class HttpRequestController {
	
    @Autowired
    private HttpRequestService httpRequestService;
    
	
   //create and update HttpRequest
   @PostMapping("/{testPlanId}/{threadGroupId}/{controllerId}")
   public ResponseDTO createHttpRequest(@RequestBody HttpRequestDto requestDto, @PathVariable String testPlanId, @PathVariable String threadGroupId,@PathVariable String controllerId ) {
    	return httpRequestService.createHttpRequest(requestDto,testPlanId,controllerId ,threadGroupId);
    	
   } 
   
   @PutMapping("/{testPlanId}/{threadGroupId}/{controllerId}")
   public ResponseDTO updateHttpRequest(@RequestBody HttpRequestDto requestDto, @PathVariable String testPlanId, @PathVariable String threadGroupId,@PathVariable String controllerId ) {
	   return httpRequestService.updateHttpRequest(requestDto, testPlanId, threadGroupId, controllerId);
   }
   
   //Delete HttpRequest
   @DeleteMapping("/{testPlanId}/{threadGroupId}/{httpRequestId}")
   public ResponseDTO deleteHttpRequest(@PathVariable String testPlanId, @PathVariable String threadGroupId, @PathVariable String httpRequestId) {
	   return httpRequestService.deleteHttpRequest(testPlanId, threadGroupId, httpRequestId);
   }

}
