package com.tyss.optimize.performance.service;

import org.springframework.stereotype.Service;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;

@Service
public interface StepService {
	
	ResponseDTO saveAsStep(TestPlan testPlan, String scriptId, String moduleId, String projectId, String stepInputs, String optionalParam, String libraryId, String stepGroupId);

}
