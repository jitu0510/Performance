package com.tyss.optimize.performance.service;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.dto.ThreadGroup;

public interface ThreadGroupService {
	
    ResponseDTO saveThreadGroup(ThreadGroup threadGroup, String testPlanId);
    
    ResponseDTO updateThreadGroup(ThreadGroup threadGroup,String testPlanId, String threadGroupId);
    
    ResponseDTO deleteThreadGroup(String testPlanId, String threadGroupId);

}
