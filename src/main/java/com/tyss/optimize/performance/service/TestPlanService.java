package com.tyss.optimize.performance.service;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;

public interface TestPlanService {
	
	ResponseDTO saveTestPlan(TestPlan testPlan);

    ResponseDTO getTestPlanById(String id);
    
    ResponseDTO updateTestPlan(TestPlan testPlan);

}
