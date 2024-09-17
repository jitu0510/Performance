package com.tyss.optimize.performance.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.execution.ThreadGroupExecution;
import com.tyss.optimize.performance.service.LogicControllerService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/public/performance")
@Slf4j
public class PerformanceClientController {
	
	@Autowired
	private ThreadGroupExecution threadGroupExecution;
    

    @Autowired
    private LogicControllerService logicControllerService;
    

    @PostMapping("/execute/{testPlanId}/{threadGroupId}")
    public ResponseDTO executeThreadGroup(@PathVariable String testPlanId, @PathVariable String threadGroupId){
        return null;
    }
    
//    @GetMapping("/execute/{testPlanId}/{threadGroupId}/{httpRequestId}")
//    public ResponseDTO executeHttpRequest(@PathVariable String testPlanId, @PathVariable String threadGroupId,@PathVariable String httpRequestId) {
//    	return threadGroupExecution.executeHttpRequest(testPlanId,threadGroupId,httpRequestId);
//    }
    
    @GetMapping("/execute-testplan")
    public ResponseDTO executeTestPlan(@RequestParam String testPlanId) throws Exception {
    	return threadGroupExecution.executeTestPlan(testPlanId);
    }
    


   

  
    

   
    
    
}
