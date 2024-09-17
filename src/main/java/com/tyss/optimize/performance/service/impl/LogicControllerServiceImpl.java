package com.tyss.optimize.performance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.dto.ThreadGroup;
import com.tyss.optimize.performance.dto.controller.Controller;
import com.tyss.optimize.performance.dto.controller.ForEachController;
import com.tyss.optimize.performance.dto.controller.IfController;
import com.tyss.optimize.performance.dto.controller.OnlyOnceController;
import com.tyss.optimize.performance.dto.controller.SimpleController;
import com.tyss.optimize.performance.dto.controller.ThroughputController;
import com.tyss.optimize.performance.dto.controller.TransactionController;
import com.tyss.optimize.performance.dto.controller.WhileController;
import com.tyss.optimize.performance.repository.PerformanceRepository;
import com.tyss.optimize.performance.service.LogicControllerService;
import com.tyss.optimize.performance.utils.GenericUtil;

@Service
public class LogicControllerServiceImpl implements LogicControllerService{
	
	@Autowired
	private PerformanceRepository performanceRepository;
	
	@Autowired
	private PerformanceServiceUtil serviceUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	 @Override
	    public ResponseDTO saveLogicController(String logicController, String type , String testPlanId, String threadGroupId) {
		    ResponseDTO responseDTO = new ResponseDTO();
		    TestPlan testPlan = serviceUtil.fetchTestPlan(testPlanId);
	        if (testPlan == null) {
	            responseDTO.setErrorCode(404);
	            responseDTO.setMessage("Test Plan not found with id: " + testPlanId);
	            responseDTO.setResponseObject(null);
	            return responseDTO;
	        }
	        ThreadGroup filteredThreadGroup = serviceUtil.filterThreadGroup(testPlan.getThreadGroups(), threadGroupId);
	        
	        if (filteredThreadGroup == null) {
	            responseDTO.setErrorCode(404);
	            responseDTO.setMessage("Thread Group not found with id: " + threadGroupId);
	            responseDTO.setResponseObject(null);
	            return responseDTO;
	        }
	        if(filteredThreadGroup.getController() == null) {
	        	filteredThreadGroup.setController(new ArrayList<Controller>());
	        }
	        Controller controller = null;
	        try {
	        	String jsonString = objectMapper.writeValueAsString(logicController);
	        	GenericUtil util = new GenericUtil();
	        	//check for id
	        	
	        	String targetType = util.determineTargetType(jsonString);
	        	
	        	controller = objectMapper.convertValue(logicController, Controller.class);
	        	
	        	
	        	if(type.equalsIgnoreCase("SimpleController")) {
	        		SimpleController simpleController = objectMapper.readValue(jsonString, SimpleController.class);
	        		
	        		if(simpleController.getId() == null || simpleController.getId().isEmpty()) {
	        			simpleController.setId(UUID.randomUUID().toString());
	        		}
	        	//	filteredThreadGroup.setController(simpleController);
	        		filteredThreadGroup.getController().add(simpleController);
	        		testPlan = performanceRepository.save(testPlan);
	        		responseDTO.setResponseObject(testPlan);
	        		responseDTO.setResponseCode(200);
	        		responseDTO.setMessage("Simple Controller is updated");
	        		return responseDTO;
	        	}
	        	else if(type.equalsIgnoreCase("IfController")) {
	        		IfController ifController = objectMapper.readValue(jsonString, IfController.class);
	        		if(ifController.getId() == null || ifController.getId().isEmpty()) {
	        			ifController.setId(UUID.randomUUID().toString());
	        		}
	        		//filteredThreadGroup.setController(ifController);
	        		filteredThreadGroup.getController().add(ifController);
	        		testPlan = performanceRepository.save(testPlan);
	        		responseDTO.setResponseObject(testPlan);
	        		responseDTO.setResponseCode(200);
	        		responseDTO.setMessage("If Controller is updated");
	        		return responseDTO;	
	        	}else if(type.equalsIgnoreCase("ForEachController")) {
	        		ForEachController forEachController = objectMapper.readValue(jsonString, ForEachController.class);
	        		if(forEachController.getId() == null || forEachController.getId().isEmpty()) {
	        			forEachController.setId(UUID.randomUUID().toString());
	        		}
	        		//filteredThreadGroup.setController(forEachController);
	        		filteredThreadGroup.getController().add(forEachController);
	        		testPlan = performanceRepository.save(testPlan);
	        		responseDTO.setResponseObject(testPlan);
	        		responseDTO.setResponseCode(200);
	        		responseDTO.setMessage("ForEach Controller is updated");
	        		return responseDTO;	
	        	}else if(type.equalsIgnoreCase("OnlyOnceController")) {
	        		OnlyOnceController onlyOnceController = objectMapper.readValue(jsonString, OnlyOnceController.class);
	        		if(onlyOnceController.getId() == null || onlyOnceController.getId().isEmpty()) {
	        			onlyOnceController.setId(UUID.randomUUID().toString());
	        		}
	        		//filteredThreadGroup.setController(onlyOnceController);
	        		filteredThreadGroup.getController().add(onlyOnceController);
	        		testPlan = performanceRepository.save(testPlan);
	        		responseDTO.setResponseObject(testPlan);
	        		responseDTO.setResponseCode(200);
	        		responseDTO.setMessage("Only Once Controller is updated");
	        		return responseDTO;	
	        	}else if(type.equalsIgnoreCase("ThroughputController")) {
	        		ThroughputController throughputController = objectMapper.readValue(jsonString, ThroughputController.class);
	        		if(throughputController.getId() == null || throughputController.getId().isEmpty()) {
	        			throughputController.setId(UUID.randomUUID().toString());
	        		}
	        		//filteredThreadGroup.setController(throughputController);
	        		filteredThreadGroup.getController().add(throughputController);
	        		testPlan = performanceRepository.save(testPlan);
	        		responseDTO.setResponseObject(testPlan);
	        		responseDTO.setResponseCode(200);
	        		responseDTO.setMessage("Throughput Controller is updated");
	        		return responseDTO;	
	        	}else if(type.equalsIgnoreCase("TransactionController")) {
	        		TransactionController transactionController = objectMapper.readValue(jsonString, TransactionController.class);
	        		if(transactionController.getId() == null || transactionController.getId().isEmpty()) {
	        			transactionController.setId(UUID.randomUUID().toString());
	        		}
	        		//filteredThreadGroup.setController(transactionController);
	        		filteredThreadGroup.getController().add(transactionController);
	        		testPlan = performanceRepository.save(testPlan);
	        		responseDTO.setResponseObject(testPlan);
	        		responseDTO.setResponseCode(200);
	        		responseDTO.setMessage("Transaction Controller is updated");
	        		return responseDTO;	
	        	}else if(type.equalsIgnoreCase("WhileController")) {
	        		WhileController whileController = objectMapper.readValue(jsonString, WhileController.class);
	        		if(whileController.getId() == null || whileController.getId().isEmpty()) {
	        			whileController.setId(UUID.randomUUID().toString());
	        		}
	        		//filteredThreadGroup.setController(whileController);
	        		filteredThreadGroup.getController().add(whileController);
	        		testPlan = performanceRepository.save(testPlan);
	        		responseDTO.setResponseObject(testPlan);
	        		responseDTO.setResponseCode(200);
	        		responseDTO.setMessage("While Controller is updated");
	        		return responseDTO;	
	        	}else {
	        		responseDTO.setErrorCode(400);
	        		responseDTO.setMessage("Invalid Controller Request Body");
	        	}	
	        }catch (Exception e) {
				throw new RuntimeException(e);
			}
	        	        
	       // filteredThreadGroup.setController(logicController);
	       
	        filteredThreadGroup.getController().add(controller);
	        testPlan.setThreadGroups(testPlan.getThreadGroups());

	        responseDTO.setResponseObject(performanceRepository.save(testPlan));
	        responseDTO.setResponseCode(200);
	        responseDTO.setMessage("Logic Controller is updated");
	        return responseDTO;
	    }

	 @Override
		public ResponseDTO deleteLogicController(String testPlanId, String threadGroupId) {
			ResponseDTO responseDTO = new ResponseDTO();
			TestPlan testPlan = serviceUtil.fetchTestPlan(testPlanId);
			List<ThreadGroup> threadGroups = testPlan.getThreadGroups();
			ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
			threadGroup.setController(null);
			threadGroups.set(threadGroups.indexOf(threadGroup), threadGroup);
			testPlan.setThreadGroups(threadGroups);
			testPlan =performanceRepository.save(testPlan);
			responseDTO.setResponseObject(testPlan);
			responseDTO.setResponseCode(200);
			
			return responseDTO;		
		}

	

	@Override
	public ResponseDTO updateLogicController(Controller logicController, String testPlanId, String threadGroupId) {

		ResponseDTO responseDTO = new ResponseDTO();
		TestPlan testPlan = serviceUtil.fetchTestPlan(testPlanId);
		List<ThreadGroup> threadGroups = testPlan.getThreadGroups();
		ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
		List<Controller> controllers = threadGroup.getController();
		int index = -1;
		for(Controller controller : controllers) {
			if(controller.getId().equals(logicController.getId())) {
				index = controllers.indexOf(controller);
			}
		}
		Object existingLogicController = threadGroup.getController();
		if(existingLogicController instanceof SimpleController) {
			SimpleController existingSimpleController = (SimpleController) existingLogicController;
			SimpleController simpleController = (SimpleController) logicController;
			existingSimpleController.setSimpleControllerName(simpleController.getSimpleControllerName());
			threadGroup.getController().set(index, existingSimpleController);
		}else if(existingLogicController instanceof ForEachController) {
			ForEachController existingForEachController = (ForEachController) existingLogicController;
			ForEachController forEachController = (ForEachController) logicController;
			existingForEachController.setForEachControllerName(forEachController.getForEachControllerName());
			existingForEachController.setInputVariableName(forEachController.getInputVariableName());
			existingForEachController.setOutputVariableName(forEachController.getOutputVariableName());
			existingForEachController.setStartIndex(forEachController.getStartIndex());
			existingForEachController.setEndIndex(forEachController.getEndIndex());
			threadGroup.getController().set(index, existingForEachController);
		}else if(existingLogicController instanceof IfController) {
			IfController existingIfController = (IfController) existingLogicController;
			IfController ifController = (IfController) logicController;
			existingIfController.setIfControllerName(ifController.getIfControllerName());
			existingIfController.setExpression(ifController.getExpression());
			threadGroup.getController().set(index, existingIfController);
		}else if(existingLogicController instanceof OnlyOnceController) {
			OnlyOnceController existingOnlyOnceController = (OnlyOnceController) existingLogicController;
			OnlyOnceController onlyOnceController = (OnlyOnceController) logicController;
			existingOnlyOnceController.setOnlyOnceControllerName(onlyOnceController.getOnlyOnceControllerName());
			threadGroup.getController().set(index, existingOnlyOnceController);
		}else if(existingLogicController instanceof ThroughputController) {
			ThroughputController existingThroughputController = (ThroughputController) existingLogicController;
			ThroughputController throughputController = (ThroughputController) logicController;
			existingThroughputController.setThroughputControllerName(throughputController.getThroughputControllerName());
			existingThroughputController.setThroughput(throughputController.getThroughput());
			existingThroughputController.setBasedOn(throughputController.getBasedOn());
			threadGroup.getController().set(index, existingThroughputController);
		}else if(existingLogicController instanceof TransactionController) {
			TransactionController existingTransactionController = (TransactionController) existingLogicController;
			TransactionController transactionController = (TransactionController) logicController;
			existingTransactionController.setTransactionControllerName(transactionController.getTransactionControllerName());
			threadGroup.getController().set(index, existingTransactionController);
		}else if(existingLogicController instanceof WhileController) {
			WhileController existingWhileController = (WhileController) existingLogicController;
			WhileController whileController = (WhileController) logicController;
			existingWhileController.setWhileControllerName(whileController.getWhileControllerName());
			existingWhileController.setCondition(whileController.getCondition());
			threadGroup.getController().set(index, existingWhileController);
		}else {
			throw new RuntimeException("Invalid Controller Request Body");
		}
		
		threadGroups.set(threadGroups.indexOf(threadGroup), threadGroup);
		testPlan.setThreadGroups(threadGroups);
		
		testPlan = performanceRepository.save(testPlan);
		
		responseDTO.setResponseObject(testPlan);
		responseDTO.setResponseCode(200);
		responseDTO.setMessage("Controller is Updated");
		
		return responseDTO;
	}
}
