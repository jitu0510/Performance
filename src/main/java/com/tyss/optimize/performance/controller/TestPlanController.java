package com.tyss.optimize.performance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.service.TestPlanService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/public/performance/testplan")
@Slf4j
public class TestPlanController {

	@Autowired 
    private TestPlanService testPlanService;
	

    @PostMapping
    public ResponseDTO saveTestPlan(@RequestBody TestPlan testPlan,@RequestParam(required = false) String moduleId, @RequestParam(required = false) String scriptId, @RequestParam String projectId, @RequestPart String stepInputs, @RequestPart(required = false) String optionalParam, @RequestParam(required = false) String libraryId, @RequestParam(required = false) String stepGroupId){
        if(testPlan.getId() == null)
            return testPlanService.saveTestPlan(testPlan);
        else
            return testPlanService.updateTestPlan(testPlan);
    }

    @GetMapping
    public ResponseDTO getTestPlanById(@RequestParam String testPlanId){
        return testPlanService.getTestPlanById(testPlanId);
    }
}
