package com.tyss.optimize.performance.service;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.controller.Controller;

public interface LogicControllerService {
	
	ResponseDTO updateLogicController(Controller logicController, String testPlanId, String threadGroupId);
	
	ResponseDTO deleteLogicController(String testPlanId, String threadGroupId);
	
	ResponseDTO saveLogicController(String logicController,String type, String testPlanId, String threadGroupId);

}
