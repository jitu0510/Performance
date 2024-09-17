package com.tyss.optimize.performance.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.regexp.recompile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PerformanceServiceUtil {
	
	@Autowired
	private PerformanceRepository performanceRepository;
	
	public List<HttpRequestDto> updateRequestDto(List<HttpRequestDto> requestDtos, HttpRequestDto requestDto) {
		String httpRequestId = requestDto.getId();
		HttpRequestDto existingRequestDto = filterHttpRequestDto(requestDtos, httpRequestId);
	
		existingRequestDto.setAssertions(requestDto.getAssertions());
		existingRequestDto.setBaseUrl(requestDto.getBaseUrl());
		existingRequestDto.setFilePath(requestDto.getFilePath());
		existingRequestDto.setHeaders(requestDto.getHeaders());
		existingRequestDto.setHttpRequestBody(requestDto.getHttpRequestBody());
		existingRequestDto.setHttpRequestMethod(requestDto.getHttpRequestMethod());
		existingRequestDto.setHttpRequestName(requestDto.getHttpRequestName());
		existingRequestDto.setHttpRequestTestData(requestDto.getHttpRequestTestData());
		existingRequestDto.setScripts(requestDto.getScripts());
		existingRequestDto.setSettings(requestDto.getSettings());
		requestDtos.set(requestDtos.indexOf(existingRequestDto), existingRequestDto);
		return requestDtos;
	}
	
	public TestPlan fetchTestPlan(String testPlanId) {
		Optional<TestPlan> optionalTestPlan = performanceRepository.findById(testPlanId);
		if(optionalTestPlan.isPresent())
			return optionalTestPlan.get();
		return null;
	}
	
	public ThreadGroup filterThreadGroup(List<ThreadGroup> threadGroups, String threadGroupId) {
		return threadGroups.stream().filter(tg -> tg.getThreadGroupId().equals(threadGroupId)).findFirst().orElse(null);
	}
	
	public HttpRequestDto filterHttpRequestDto(List<HttpRequestDto> requestDtos, String httpRequestId) {
		return requestDtos.stream().filter(hrd -> hrd.getId().equals(httpRequestId)).findFirst().orElse(null);
	}
	
	public ThreadGroup setControllerInThreadGroup(ThreadGroup threadGroup,List<ThreadGroup> threadGroups,String httpRequestId,Object controller,List<HttpRequestDto> requestDtos,ResponseDTO responseDTO) {
		if(controller instanceof SimpleController) {
			SimpleController simpleController = (SimpleController) controller;
			requestDtos = simpleController.getHttpRequestDto();
			HttpRequestDto requestDto  = filterHttpRequestDto(requestDtos, httpRequestId);
			if(requestDto == null)
				responseDTO.setErrorCode(404);
			requestDtos.remove(requestDto);
			simpleController.setHttpRequestDto(requestDtos);
			threadGroup.getController().add(simpleController);
			//threadGroup.setController(simpleController);
		}else if(controller instanceof ForEachController) {
			ForEachController forEachController = (ForEachController) controller;
			requestDtos = forEachController.getHttpRequestDto();
			HttpRequestDto requestDto  = filterHttpRequestDto(requestDtos, httpRequestId);
			if(requestDto == null)
				responseDTO.setErrorCode(404);
			requestDtos.remove(requestDto);
			forEachController.setHttpRequestDto(requestDtos);
			//threadGroup.setController(forEachController);
			threadGroup.getController().add(forEachController);
		}else if(controller instanceof IfController) {
			IfController ifController = (IfController) controller;
			requestDtos = ifController.getHttpRequestDto();
			HttpRequestDto requestDto  = filterHttpRequestDto(requestDtos, httpRequestId);
			if(requestDto == null)
				responseDTO.setErrorCode(404);
			requestDtos.remove(requestDto);
			ifController.setHttpRequestDto(requestDtos);
			//threadGroup.setController(ifController);
			threadGroup.getController().add(ifController);
		}else if(controller instanceof OnlyOnceController) {
			OnlyOnceController onceController = (OnlyOnceController) controller;
			requestDtos = onceController.getHttpRequestDto();
			HttpRequestDto requestDto  = filterHttpRequestDto(requestDtos, httpRequestId);
			if(requestDto == null)
				responseDTO.setErrorCode(404);
			requestDtos.remove(requestDto);
			onceController.setHttpRequestDto(requestDtos);
			//threadGroup.setController(onceController);
			threadGroup.getController().add(onceController);
		}else if(controller instanceof ThroughputController) {
			ThroughputController throughputController = (ThroughputController) controller;
			requestDtos = throughputController.getHttpRequestDto();
			HttpRequestDto requestDto  = filterHttpRequestDto(requestDtos, httpRequestId);
			if(requestDto == null)
				responseDTO.setErrorCode(404);
			requestDtos.remove(requestDto);
			throughputController.setHttpRequestDto(requestDtos);
			//threadGroup.setController(throughputController);
			threadGroup.getController().add(throughputController);
		}else if(controller instanceof TransactionController) {
			TransactionController transactionController = (TransactionController) controller;
			requestDtos = transactionController.getHttpRequestDto();
			HttpRequestDto requestDto  = filterHttpRequestDto(requestDtos, httpRequestId);
			if(requestDto == null)
				responseDTO.setErrorCode(404);
			requestDtos.remove(requestDto);
			transactionController.setHttpRequestDto(requestDtos);
			//threadGroup.setController(transactionController);
			threadGroup.getController().add(transactionController);
		}else if(controller instanceof WhileController) {
			WhileController whileController = (WhileController) controller;
			requestDtos = whileController.getHttpRequestDto();
			HttpRequestDto requestDto  = filterHttpRequestDto(requestDtos, httpRequestId);
			if(requestDto == null)
				responseDTO.setErrorCode(404);
			requestDtos.remove(requestDto);
			whileController.setHttpRequestDto(requestDtos);
			//threadGroup.setController(whileController);
			threadGroup.getController().add(whileController);
		}else {
			responseDTO.setErrorCode(500);
		}
		return threadGroup;
	}

	public Object filterController(List<Controller> controllers, String controllerId) {
		return controllers.stream().filter(c -> c.getId().equals(controllerId)).findFirst().orElse(null);
	}

}
