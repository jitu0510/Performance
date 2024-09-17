
package com.tyss.optimize.performance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.dto.ThreadGroup;
import com.tyss.optimize.performance.service.ThreadGroupService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/public/performance/thread-group")
@Slf4j
public class ThreadGroupController {
	
	@Autowired
	private ThreadGroupService threadGroupService;
	
    //Save Thread Group
    @PostMapping
    public ResponseDTO saveThreadGroup(@RequestBody ThreadGroup threadGroup, @RequestParam String testPlanId){
            return threadGroupService.saveThreadGroup(threadGroup,testPlanId);
    }

    @PutMapping
    public ResponseDTO updateThreadGroup(@RequestBody ThreadGroup threadGroup, @RequestParam String testPlanId, @RequestParam String threadGroupId){
        return  threadGroupService.updateThreadGroup(threadGroup, testPlanId, threadGroupId);
    }
    
   // @DeleteMapping("/{testPlanId}/{threadGroupId}")
    public ResponseDTO deleteThreadGroup(@PathVariable String testPlanId, @PathVariable String threadGroupId) {
    	return threadGroupService.deleteThreadGroup(testPlanId, threadGroupId);
    }

}
