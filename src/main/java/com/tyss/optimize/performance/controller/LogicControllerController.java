package com.tyss.optimize.performance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.controller.Controller;
import com.tyss.optimize.performance.service.LogicControllerService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/public/performance/logic-controller")
@Slf4j
public class LogicControllerController {
	
	@Autowired
    private LogicControllerService logicControllerService;
	
	 //Save a Logic Controller
    @PostMapping
    public ResponseDTO saveLogicController(@RequestBody String logicController, @RequestParam String type , @RequestParam String testPlanId, @RequestParam String threadGroupId){
        return logicControllerService.saveLogicController(logicController,type,testPlanId,threadGroupId);
    }

    //update a logic controller
    @PutMapping
    public ResponseDTO updateLogicController(@RequestBody Controller logicController, @RequestParam String testPlanId,@RequestParam String threadGroupId){
        return logicControllerService.updateLogicController(logicController,testPlanId,threadGroupId);
    }
    
    @DeleteMapping
    public ResponseDTO deleteLogicController(@RequestParam String testPlanId,@RequestParam String threadGroupId) {
    	return logicControllerService.deleteLogicController(testPlanId, threadGroupId);
    }
    

}
