package com.tyss.optimize.performance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyss.optimize.performance.dto.HttpRequestDto;
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
import com.tyss.optimize.performance.service.HttpRequestService;
import com.tyss.optimize.performance.utils.GenericUtil;

import ch.qos.logback.classic.util.CopyOnInheritThreadLocal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HttpRequestServiceImpl implements HttpRequestService{
	
	@Autowired
	private PerformanceRepository performanceRepository;
	
	@Autowired
	private PerformanceServiceUtil serviceUtil;
	
	 @Autowired 
	 private ObjectMapper objectMapper;

	@Override
	public ResponseDTO deleteHttpRequest(String testPlanId, String threadGroupId, String httpRequestId) {
		ResponseDTO responseDTO = new ResponseDTO();
		TestPlan testPlan = serviceUtil.fetchTestPlan(testPlanId);
		List<ThreadGroup> threadGroups = testPlan.getThreadGroups();
		ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
		Object controller = threadGroup.getController();
		List<HttpRequestDto> requestDtos = null;
		threadGroup = serviceUtil.setControllerInThreadGroup(threadGroup, threadGroups, httpRequestId, controller, requestDtos,responseDTO);
		if(responseDTO.getErrorCode() != 0) {
			if(responseDTO.getErrorCode() == 404) {
				responseDTO.setMessage("Invalid HttpRequest Id");
			}else {
				responseDTO.setMessage("Invalid Controller Body");
			}
			return responseDTO;
		}
		threadGroups.set(threadGroups.indexOf(threadGroup), threadGroup);
		testPlan.setThreadGroups(threadGroups);
		testPlan = performanceRepository.save(testPlan);
		
		log.info("TEST PLAN: "+testPlan);
		responseDTO.setResponseObject(testPlan);
		responseDTO.setResponseCode(200);
		responseDTO.setMessage("Http Request Deleted Successfully");
		return responseDTO;
	}
	
/*	@Override
	public ResponseDTO updateHttpRequest(HttpRequestDto requestDto, String testPlanId, String threadGroupId) {
		ResponseDTO responseDTO = new ResponseDTO();
		TestPlan testPlan = serviceUtil.fetchTestPlan(testPlanId);
		List<ThreadGroup> threadGroups = testPlan.getThreadGroups();
		ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
		List<Controller> controllers = threadGroup.getController();
		List<HttpRequestDto> requestDtos = null;
		PerformanceServiceUtil serviceUtil = new PerformanceServiceUtil();
		int index = -1;
		for(Controller controller : controllers) {
			controller = (Controller) controller;
			if(controller.getId())
		}
		if(controller instanceof SimpleController) {
			SimpleController simpleController = (SimpleController) controller; 
			requestDtos = simpleController.getHttpRequestDto();
			requestDtos = serviceUtil.updateRequestDto(requestDtos, requestDto);
			simpleController.setHttpRequestDto(requestDtos);
			for(var controller : controllers) {
				
			}
			threadGroup.setController(simpleController);
		}else if(controller instanceof ForEachController) {
			ForEachController forEachController = (ForEachController) controller;
			requestDtos = forEachController.getHttpRequestDto();
			requestDtos = serviceUtil.updateRequestDto(requestDtos,` requestDto);
			forEachController.setHttpRequestDto(requestDtos);
			threadGroup.setController(forEachController);
		}else if(controller instanceof IfController) {
			IfController ifController = (IfController) controller;
			requestDtos = ifController.getHttpRequestDto();
			requestDtos = serviceUtil.updateRequestDto(requestDtos, requestDto);
			ifController.setHttpRequestDto(requestDtos);
			threadGroup.setController(ifController);
		}else if(controller instanceof OnlyOnceController) {
			OnlyOnceController onlyOnceController = (OnlyOnceController) controller;
			requestDtos = onlyOnceController.getHttpRequestDto();
			requestDtos = serviceUtil.updateRequestDto(requestDtos, requestDto);
			onlyOnceController.setHttpRequestDto(requestDtos);
			threadGroup.setController(onlyOnceController);
		}else if(controller instanceof ThroughputController) {
			ThroughputController throughputController = (ThroughputController) controller;
			requestDtos = throughputController.getHttpRequestDto();
			requestDtos = serviceUtil.updateRequestDto(requestDtos, requestDto);
			throughputController.setHttpRequestDto(requestDtos);
			threadGroup.setController(throughputController);
		}else if(controller instanceof TransactionController) {
			TransactionController transactionController = (TransactionController) controller;
			transactionController.getHttpRequestDto();
			requestDtos = serviceUtil.updateRequestDto(requestDtos, requestDto);
			transactionController.setHttpRequestDto(requestDtos);
			threadGroup.setController(transactionController);
		}else if(controller instanceof WhileController) {
			WhileController whileController = (WhileController) controller;
			requestDtos = whileController.getHttpRequestDto();
			requestDtos = serviceUtil.updateRequestDto(requestDtos, requestDto);
			whileController.setHttpRequestDto(requestDtos);
			threadGroup.setController(whileController);
		}else {
			throw new RuntimeException("Invalid Controller Type");
		}
		
		threadGroups.set(threadGroups.indexOf(threadGroup), threadGroup);
		testPlan.setThreadGroups(threadGroups);
		testPlan = performanceRepository.save(testPlan);
		responseDTO.setResponseObject(testPlan);
		responseDTO.setResponseCode(200);
		responseDTO.setMessage("Http Request Updated Successfully");
		System.out.println("Saved Test Plan: "+testPlan);
		return responseDTO;
	}*/
	
	@Override
	public ResponseDTO createHttpRequest(HttpRequestDto requestDto, String testPlanId,String controllerId, String threadGroupId) {

		ResponseDTO responseDTO = new ResponseDTO();
		TestPlan testPlan = serviceUtil.fetchTestPlan(testPlanId);
		List<ThreadGroup> threadGroups = testPlan.getThreadGroups();
		ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
		List<Controller> controllers = threadGroup.getController();
		Controller controller = (Controller) serviceUtil.filterController(controllers,controllerId);
		//find type of controller
		try {
		String jsonString = objectMapper.writeValueAsString(controller);
		SimpleController simpleController = null;
		IfController ifController = null;
		ForEachController forEachController = null;
		OnlyOnceController onlyOnceController = null;
		ThroughputController throughputController = null;
		WhileController whileController = null;
		TransactionController transactionController = null;
		GenericUtil genericUtil = new GenericUtil();
		String targetType = genericUtil.determineTargetType(jsonString);
		if(targetType.equalsIgnoreCase("SimpleController")) {
			simpleController = objectMapper.readValue(jsonString, SimpleController.class);
			List<HttpRequestDto> requestDtos = simpleController.getHttpRequestDto();
			if(requestDtos == null) {
				requestDtos = new ArrayList<HttpRequestDto>();
			}
			if(requestDto.getId() == null || requestDto.getId().isEmpty()) {
				requestDto.setId(UUID.randomUUID().toString());
			} 
			
			requestDtos.add(requestDto); 
			simpleController.setHttpRequestDto(requestDtos);
			controllers.set(controllers.indexOf(controller), simpleController);
			
		}else if(targetType.equalsIgnoreCase("IfController")) {
			ifController = objectMapper.readValue(jsonString, IfController.class);
			List<HttpRequestDto> requestDtos = ifController.getHttpRequestDto();
			if(requestDtos == null) {
				requestDtos = new ArrayList<HttpRequestDto>();
			}
			if(requestDto.getId() == null || requestDto.getId().isEmpty()) {
				requestDto.setId(UUID.randomUUID().toString());
			} 

			//List<Controller> controllers = threadGroup.getController();
			if(controllers != null)
				controllers.add(ifController);
			else {	
				controllers = new ArrayList<Controller>();
				controllers.add(ifController);
				
			}
			requestDtos.add(requestDto); 
			ifController.setHttpRequestDto(requestDtos);
			controllers.set(controllers.indexOf(controller), ifController);
			
		}else if(targetType.equalsIgnoreCase("ForEachController")) {
			forEachController = objectMapper.readValue(jsonString, ForEachController.class);
			List<HttpRequestDto> requestDtos = forEachController.getHttpRequestDto();
			if(requestDtos == null) {
				requestDtos = new ArrayList<HttpRequestDto>();
			}
			if(requestDto.getId() == null || requestDto.getId().isEmpty()) {
				requestDto.setId(UUID.randomUUID().toString());
			} 
			
			//List<Controller> controllers = threadGroup.getController();
			if(controllers != null)
				controllers.add(forEachController);
			else {	
				controllers = new ArrayList<Controller>();
				controllers.add(forEachController);
				
			}
			requestDtos.add(requestDto); 
			forEachController.setHttpRequestDto(requestDtos);
			controllers.set(controllers.indexOf(controller), forEachController);
		}else if(targetType.equalsIgnoreCase("OnlyOnceController")) {
			onlyOnceController = objectMapper.readValue(jsonString, OnlyOnceController.class);
			List<HttpRequestDto> requestDtos = onlyOnceController.getHttpRequestDto();
			if(requestDtos == null) {
				requestDtos = new ArrayList<HttpRequestDto>();
			}
			if(requestDto.getId() == null || requestDto.getId().isEmpty()) {
				requestDto.setId(UUID.randomUUID().toString());
			} 
			
		//	List<Controller> controllers = threadGroup.getController();
			if(controllers != null)
				controllers.add(onlyOnceController);
			else {	
				controllers = new ArrayList<Controller>();
				controllers.add(onlyOnceController);
				
			}
			requestDtos.add(requestDto); 
			onlyOnceController.setHttpRequestDto(requestDtos);
			controllers.set(controllers.indexOf(controller), onlyOnceController);
			
		}else if(targetType.equalsIgnoreCase("ThroughputController")) {
			throughputController = objectMapper.readValue(jsonString, ThroughputController.class);
			List<HttpRequestDto> requestDtos = throughputController.getHttpRequestDto();
			if(requestDtos == null) {
				requestDtos = new ArrayList<HttpRequestDto>();
			}
			if(requestDto.getId() == null || requestDto.getId().isEmpty()) {
				requestDto.setId(UUID.randomUUID().toString());
			} 
			
			//List<Controller> controllers = threadGroup.getController();
			if(controllers != null)
				controllers.add(throughputController);
			else {	
				controllers = new ArrayList<Controller>();
				controllers.add(throughputController);
				
			}
			requestDtos.add(requestDto); 
			throughputController.setHttpRequestDto(requestDtos);
			controllers.set(controllers.indexOf(controller), throughputController);
		}else if(targetType.equalsIgnoreCase("TransactionController")) {
			transactionController = objectMapper.readValue(jsonString, TransactionController.class);
			List<HttpRequestDto> requestDtos = transactionController.getHttpRequestDto();
			if(requestDtos == null) {
				requestDtos = new ArrayList<HttpRequestDto>();
			}
			if(requestDto.getId() == null || requestDto.getId().isEmpty()) {
				requestDto.setId(UUID.randomUUID().toString());
			} 
			

			//List<Controller> controllers = threadGroup.getController();
			if(controllers != null)
				controllers.add(transactionController);
			else {	
				controllers = new ArrayList<Controller>();
				controllers.add(transactionController);
				
			}
			requestDtos.add(requestDto); 
			transactionController.setHttpRequestDto(requestDtos);
			controllers.set(controllers.indexOf(controller), transactionController);
		}else if(targetType.equalsIgnoreCase("WhileController")) {
			whileController = objectMapper.readValue(jsonString, WhileController.class);
			List<HttpRequestDto> requestDtos = whileController.getHttpRequestDto();
			if(requestDtos == null) {
				requestDtos = new ArrayList<HttpRequestDto>();
			}
			if(requestDto.getId() == null || requestDto.getId().isEmpty()) {
				requestDto.setId(UUID.randomUUID().toString());
			} 
			

			//List<Controller> controllers = threadGroup.getController();
			if(controllers != null)
				controllers.add(whileController);
			else {	
				controllers = new ArrayList<Controller>();
				controllers.add(whileController);
				
			}
			requestDtos.add(requestDto); 
			whileController.setHttpRequestDto(requestDtos);
			controllers.set(controllers.indexOf(controller), whileController);
		}else {
			throw new RuntimeException("Unknown Controller");
		}
		threadGroups.set(threadGroups.indexOf(threadGroup), threadGroup);
		testPlan.setThreadGroups(threadGroups);
		testPlan = performanceRepository.save(testPlan);
		System.out.println("TestPlan"+testPlan);
		responseDTO.setResponseObject(testPlan);
		responseDTO.setResponseCode(200);
		responseDTO.setMessage("Http Request Added");
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return responseDTO;
	}

	@Override
	public ResponseDTO updateHttpRequest(TestPlan testPlan) {
		ResponseDTO responseDTO = new ResponseDTO();
		Optional<TestPlan> optionalDBTestPlan = performanceRepository.findById(testPlan.getId());
		TestPlan DBTestPlan = optionalDBTestPlan.get();
		DBTestPlan.setThreadGroups(testPlan.getThreadGroups());
		try {
		TestPlan savedTestPlan = performanceRepository.save(DBTestPlan);
		responseDTO.setResponseObject(savedTestPlan);
		responseDTO.setResponseCode(200);
		responseDTO.setMessage("Success");
		}catch (Exception e) {
			responseDTO.setMessage(e.getMessage());
			responseDTO.setStatus("Failed");
		}
		return responseDTO;
		
		
	}

	@Override
	public ResponseDTO updateHttpRequest(HttpRequestDto requestDto, String testPlanId, String threadGroupId,
			String controllerId) {
		ResponseDTO responseDTO = new ResponseDTO();
		TestPlan testPlan = serviceUtil.fetchTestPlan(testPlanId);
		List<ThreadGroup> threadGroups = testPlan.getThreadGroups();
		ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
		List<Controller> controllers = threadGroup.getController();
		Controller controller = (Controller) serviceUtil.filterController(controllers,controllerId);
		List<HttpRequestDto> requestDtos = null;
		if(controller instanceof SimpleController) {
			SimpleController simpleController = (SimpleController) controller;
			requestDtos  = simpleController.getHttpRequestDto();
			HttpRequestDto dbRequestDto = requestDtos.stream().filter(request -> request.getId().equals(requestDto.getId())).findFirst().get();
			setRequestDto(requestDto, dbRequestDto);
			controllers.set(controllers.indexOf(controller), simpleController);
		}else if(controller instanceof TransactionController) {
			TransactionController transactionController = (TransactionController) controller;
			requestDtos = transactionController.getHttpRequestDto();
			HttpRequestDto dbRequestDto = requestDtos.stream().filter(request -> request.getId().equals(requestDto.getId())).findFirst().get();
			setRequestDto(requestDto, dbRequestDto);
			controllers.set(controllers.indexOf(controller), transactionController);
		}else if(controller instanceof OnlyOnceController) {
			OnlyOnceController onlyOnceController = (OnlyOnceController) controller;
			requestDtos = onlyOnceController.getHttpRequestDto();
			HttpRequestDto dbRequestDto = requestDtos.stream().filter(request -> request.getId().equals(requestDto.getId())).findFirst().get();
			setRequestDto(requestDto, dbRequestDto);
			controllers.set(controllers.indexOf(controller), onlyOnceController);
		}else if(controller instanceof IfController) {
			IfController ifController = (IfController) controller;
			requestDtos = ifController.getHttpRequestDto();
			HttpRequestDto dbRequestDto = requestDtos.stream().filter(request -> request.getId().equals(requestDto.getId())).findFirst().get();
			setRequestDto(requestDto, dbRequestDto);
			controllers.set(controllers.indexOf(controller), ifController);
		}else if(controller instanceof WhileController) {
			WhileController whileController = (WhileController) controller;
			requestDtos = whileController.getHttpRequestDto();
			HttpRequestDto dbRequestDto = requestDtos.stream().filter(request -> request.getId().equals(requestDto.getId())).findFirst().get();
			setRequestDto(requestDto, dbRequestDto);
			controllers.set(controllers.indexOf(controller), whileController);
		}else if(controller instanceof ThroughputController) {
			ThroughputController throughputController = (ThroughputController) controller;
			requestDtos = throughputController.getHttpRequestDto();
			HttpRequestDto dbRequestDto = requestDtos.stream().filter(request -> request.getId().equals(requestDto.getId())).findFirst().get();
			setRequestDto(requestDto, dbRequestDto);
			controllers.set(controllers.indexOf(controller), throughputController);
		}else if(controller instanceof ForEachController) {
			ForEachController forEachController = (ForEachController) controller;
			requestDtos = forEachController.getHttpRequestDto();
			HttpRequestDto dbRequestDto = requestDtos.stream().filter(request -> request.getId().equals(requestDto.getId())).findFirst().get();
			setRequestDto(requestDto, dbRequestDto);
			controllers.set(controllers.indexOf(controller), forEachController);
		}else {
			responseDTO.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseDTO.setMessage("Invalid controller Type");
			return responseDTO;
		}
		
		performanceRepository.save(testPlan);
		responseDTO.setResponseObject(testPlan);
		responseDTO.setResponseCode(HttpStatus.OK.value());
		return responseDTO;
	}

	public void setRequestDto(HttpRequestDto requestDto, HttpRequestDto dbRequestDto) {
		dbRequestDto.setAssertions(requestDto.getAssertions());
		dbRequestDto.setBaseUrl(requestDto.getBaseUrl());
		dbRequestDto.setFilePath(requestDto.getFilePath());
		dbRequestDto.setHeaders(requestDto.getHeaders());
		dbRequestDto.setHttpRequestBody(requestDto.getHttpRequestBody());
		dbRequestDto.setHttpRequestMethod(requestDto.getHttpRequestMethod());
		dbRequestDto.setHttpRequestName(requestDto.getHttpRequestName());
		dbRequestDto.setHttpRequestTestData(requestDto.getHttpRequestTestData());
		dbRequestDto.setScripts(requestDto.getScripts());
		dbRequestDto.setSettings(requestDto.getSettings());
	}
	

}
