package com.tyss.optimize.performance.execution;

import com.tyss.optimize.performance.dto.ResponseDTO;

public interface ThreadGroupExecution {
	
	ResponseDTO executeThreadGroup(String testPlanId, String threadGroupId);

	//ResponseDTO executeHttpRequest(String testPlanId, String threadGroupId, String httpRequestId);

	ResponseDTO executeTestPlan(String testPlanId) throws Exception;

}
