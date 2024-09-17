package com.tyss.optimize.performance.service;



import com.tyss.optimize.performance.dto.HttpRequestDto;
import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;


public interface HttpRequestService {
	
	ResponseDTO createHttpRequest(HttpRequestDto requestDto, String testPlanId,String controllerId ,String threadGroupId);

	//ResponseDTO updateHttpRequest(HttpRequestDto requestDto, String testPlanId, String threadGroupId);
	
	ResponseDTO deleteHttpRequest(String testPlanId, String threadGroupId, String httpRequestId);

	ResponseDTO updateHttpRequest(TestPlan testPlan);

	ResponseDTO updateHttpRequest(HttpRequestDto requestDto, String testPlanId, String threadGroupId,
			String controllerId);
	
	
	

}
