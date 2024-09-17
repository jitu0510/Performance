package com.tyss.optimize.performance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.dto.ThreadGroup;
import com.tyss.optimize.performance.dto.ThreadGroupNormalProperties;
import com.tyss.optimize.performance.repository.PerformanceRepository;
import com.tyss.optimize.performance.service.ThreadGroupService;

@Service
public class ThreadGroupServiceImpl implements ThreadGroupService{
	
	@Autowired
	private PerformanceRepository performanceRepository;
	
	@Autowired
	private PerformanceServiceUtil serviceUtil;
	
    @Override
    public ResponseDTO saveThreadGroup(ThreadGroup threadGroup, String testPlanId) {
       // Optional<TestPlan> optionalTestPlan = performanceRepository.findById(testPlanId);
        ResponseDTO responseDTO = new ResponseDTO();
        TestPlan testPlan = serviceUtil.fetchTestPlan(testPlanId);
        if(testPlan == null){
            responseDTO.setErrorCode(404);
            responseDTO.setMessage("Test Plan not found with id: "+testPlanId);
            responseDTO.setResponseObject(null);
            return responseDTO;
        }
       // TestPlan testPlan = optionalTestPlan.get();
        if(testPlan.getThreadGroups() == null || testPlan.getThreadGroups().size() == 0){
            List<ThreadGroup> threadGroups = new ArrayList<>();
            threadGroup.setThreadGroupId(UUID.randomUUID().toString());
            threadGroups.add(threadGroup);
            testPlan.setThreadGroups(threadGroups);
        }else{
                List<ThreadGroup> threadGroups = testPlan.getThreadGroups();
                threadGroup.setThreadGroupId(UUID.randomUUID().toString());
                threadGroups.add(threadGroup);
        }
        TestPlan savedTestPlan = performanceRepository.save(testPlan);
        responseDTO.setResponseCode(200);
        responseDTO.setResponseObject(savedTestPlan);
        return responseDTO;
    }
    
    @Override
    public ResponseDTO updateThreadGroup(ThreadGroup threadGroup, String testPlanId, String threadGroupId){
        Optional<TestPlan> optionalTestPlan = performanceRepository.findById(testPlanId);

        ResponseDTO responseDTO = new ResponseDTO();
        if(optionalTestPlan.isEmpty()){
            responseDTO.setErrorCode(404);
            responseDTO.setMessage("Test Plan not found with id: "+testPlanId);
            responseDTO.setResponseObject(null);
            return responseDTO;
        }
      //  TestPlan testPlan = optionalTestPlan.get();
        TestPlan testPlanEnity = optionalTestPlan.get();
        ThreadGroup threadGroupEnity = serviceUtil.filterThreadGroup(testPlanEnity.getThreadGroups(), threadGroupId);
       // ThreadGroup dbThreadGroup = serviceUtil.filterThreadGroup(testPlan.getThreadGroups(), threadGroupId);
        if ( !threadGroup.getThreadGroupName().equals(threadGroupEnity.getThreadGroupName())){
            threadGroupEnity.setThreadGroupName(threadGroup.getThreadGroupName());
        }
        if( !threadGroup.getThreadGroupProperties().equals(threadGroupEnity.getThreadGroupProperties())){
            threadGroupEnity.setThreadGroupProperties(threadGroup.getThreadGroupProperties());
        }
        if(Objects.nonNull(threadGroup.getNormalProperties()) && threadGroup.getThreadGroupRampAndHoldPropertiesList().size() > 0) {
        	//create error
        }
        if(Objects.nonNull(threadGroup.getNormalProperties())) {
        ThreadGroupNormalProperties normalProperties = threadGroup.getNormalProperties();
        threadGroupEnity.getNormalProperties().setUsers(normalProperties.getUsers());
        threadGroupEnity.getNormalProperties().setLoopCount(normalProperties.getLoopCount());
        threadGroupEnity.getNormalProperties().setDurationInSecs(normalProperties.getDurationInSecs());
        }

        if(Objects.nonNull(threadGroup.getThreadGroupRampAndHoldPropertiesList()))
        	threadGroupEnity.setThreadGroupRampAndHoldPropertiesList(threadGroup.getThreadGroupRampAndHoldPropertiesList());
        
        testPlanEnity.getThreadGroups().set(testPlanEnity.getThreadGroups().indexOf(threadGroupEnity),threadGroupEnity);

        responseDTO.setResponseObject(performanceRepository.save(testPlanEnity));
        responseDTO.setResponseCode(200);
        responseDTO.setMessage("Thread Group "+threadGroupEnity.getThreadGroupName()+" is updated");
        return responseDTO;
    }
    
	@Override
	public ResponseDTO deleteThreadGroup(String testPlanId, String threadGroupId) {
		ResponseDTO responseDTO = new ResponseDTO();
		TestPlan testPlan = serviceUtil.fetchTestPlan(testPlanId);
		
		List<ThreadGroup> threadGroups = testPlan.getThreadGroups();
		
		ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
		threadGroups.remove(threadGroup);
		
		performanceRepository.save(testPlan);
		responseDTO.setResponseObject(testPlan);
		responseDTO.setResponseCode(200);
		responseDTO.setMessage("Thread Group is Deleted Successfully");
		return responseDTO;
	}


}
