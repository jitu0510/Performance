package com.tyss.optimize.performance.execution.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.jmeter.threads.JMeterContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import com.tyss.optimize.performance.dto.HttpRequestDto;
import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.ResponsePayload;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.dto.ThreadGroup;
import com.tyss.optimize.performance.dto.controller.Controller;
import com.tyss.optimize.performance.execution.ThreadGroupExecution;
import com.tyss.optimize.performance.service.TestPlanService;
import com.tyss.optimize.performance.service.ThreadGroupService;
import com.tyss.optimize.performance.service.impl.PerformanceServiceUtil;

import lombok.extern.slf4j.Slf4j;
import us.abstracta.jmeter.javadsl.core.DslTestPlan;
import us.abstracta.jmeter.javadsl.core.DslTestPlan.TestPlanChild;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.core.threadgroups.DslDefaultThreadGroup;
import us.abstracta.jmeter.javadsl.http.DslHttpSampler;

@Service
@Slf4j
public class ThreadGroupExecutionImpl implements ThreadGroupExecution {

	@Autowired
	private TestPlanService testPlanService;

	@Autowired
	private ThreadGroupService threadGroupService;

	@Autowired
	private PerformanceServiceUtil serviceUtil;
	
	

	@Override
	public ResponseDTO executeThreadGroup(String testPlanId, String threadGroupId) {

		ResponseDTO responseDTO = new ResponseDTO();
		ResponseDTO response = testPlanService.getTestPlanById(testPlanId);
		if (response.getErrorCode() != 0) {
			if (response.getErrorCode() == 404) {
				responseDTO.setMessage("Invalid TestPlan Id");
				responseDTO.setErrorCode(404);
			} else {
				responseDTO.setErrorCode(response.getErrorCode());
				responseDTO.setMessage("Couldnot Fetch Test Plan");
			}
		}

		List<ThreadGroup> threadGroups = ((TestPlan) response.getResponseObject()).getThreadGroups();
		ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
		return null;

	}

	@Override
	public ResponseDTO executeTestPlan(String testPlanId) throws Exception {
		ResponseDTO responseDTO = new ResponseDTO();
		TestPlanStats stats = null;
		ThreadGroupExecutionUtil executionUtil = new ThreadGroupExecutionUtil();
		ResponseDTO response = testPlanService.getTestPlanById(testPlanId);
		
		if (response.getErrorCode() != 0) {
			if (response.getErrorCode() == 404) {
				responseDTO.setMessage("Invalid TestPlan Id");
				responseDTO.setErrorCode(404);
			} else {
				responseDTO.setErrorCode(response.getErrorCode());
				responseDTO.setMessage("Couldnot Fetch Test Plan");
			}
		}
		
		TestPlan testPlanDto = (TestPlan)response.getResponseObject();
		List<ThreadGroup> threadGroups = testPlanDto.getThreadGroups();
		
		//DslTestPlan testPlan = executionUtil.getTestPlan(testPlanDto.getTestPlanName());
		List<TestPlanChild> dslThreadGroups = new ArrayList<TestPlanChild>();
		for(ThreadGroup threadGroup : threadGroups) {
			DslDefaultThreadGroup dslThreadGroup = null;
			List<DslHttpSampler> samplers = new ArrayList<DslHttpSampler>();
			List<Controller> controllers =  threadGroup.getController();
		//	System.out.println(controllers);
			dslThreadGroup = new DslDefaultThreadGroup(threadGroup.getThreadGroupName());
			executionUtil.setCsvDataSet(dslThreadGroup, threadGroup.getCsvDataSet());
			if (controllers != null) {
				
				for (Controller controller : controllers) {
					List<HttpRequestDto> requestDtos = executionUtil.getRequestDto(controller);
				//	System.out.println("Request: "+requestDtos);
					for(HttpRequestDto requestDto : requestDtos) {
					DslHttpSampler sampler = executionUtil.setControllerProperties(requestDto);
					
					
					executionUtil.setController(dslThreadGroup,controller,sampler);
					executionUtil.setCsvDataSet(sampler,requestDto.getCsvDataSet());
					executionUtil.setPreProcessor(sampler, requestDto.getScripts().getPreProcessor());
					executionUtil.setJsonExtractor(sampler, requestDto.getScripts().getPostProcessor());
					executionUtil.setBoundaryExtractor(sampler, requestDto.getScripts().getPostProcessor());
					if(requestDto.getScripts().getPostProcessor().getCustomLogic() != null && ! requestDto.getScripts().getPostProcessor().getCustomLogic().isBlank()) {
						executionUtil.executePostProcessorCustomLogic(requestDto.getScripts().getPostProcessor());
					}
					executionUtil.setAssertions(sampler, requestDto.getAssertions());
					// before adding the sampler into the list, add all the properties into the
					// sample
					samplers.add(sampler);
					}
				}
			}
			
			executionUtil.setThreadGroupProperties(dslThreadGroup, threadGroup, samplers);
			dslThreadGroups.add(dslThreadGroup);
		}
		
		DslTestPlan testPlan = new DslTestPlan(dslThreadGroups);
		
		executionUtil.setDataSet(testPlan, ((TestPlan)response.getResponseObject()).getConfigElement());
		executionUtil.setLiveMonitoring(testPlan, ((TestPlan)response.getResponseObject()).getLiveMonitoring() );
		executionUtil.setReport((TestPlan)response.getResponseObject(),testPlan);
		testPlan.saveAsJmx("test.jmx"); 
		try {
			stats = testPlan.run();
			
			log.info("Duration: "+stats.duration().toMillis()+" ms");
			log.info("PerSecond: "+stats.overall().samples().perSecond()+" samples");
			log.info("Max: "+stats.overall().sampleTime().max().toMillis()+" ms");
			log.info("Min :"+stats.overall().sampleTime().min().toMillis()+" ms");
			
			ResponsePayload payload = new ResponsePayload();
			payload.setTotalDuration(stats.duration().toMillis()+" ms");
			payload.setSamplesPerSecond(stats.overall().samples().perSecond());
			payload.setMaxTime(stats.overall().sampleTime().max().toMillis()+" ms");
			payload.setMinTime(stats.overall().sampleTime().min().toMillis()+" ms");
			responseDTO.setResponseObject(payload);
			responseDTO.setResponseCode(200);
			responseDTO.setStatus("Success");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}

	
	
	
	/*@Override
	public ResponseDTO executeHttpRequest(String testPlanId, String threadGroupId, String httpRequestId) {

		ResponseDTO responseDTO = new ResponseDTO();
		ResponseDTO response = testPlanService.getTestPlanById(testPlanId);
		if (response.getErrorCode() != 0) {
			if (response.getErrorCode() == 404) {
				responseDTO.setMessage("Invalid TestPlan Id");
				responseDTO.setErrorCode(404);
			} else {
				responseDTO.setErrorCode(response.getErrorCode());
				responseDTO.setMessage("Couldnot Fetch Test Plan");
			}
		}

		List<ThreadGroup> threadGroups = ((TestPlan) response.getResponseObject()).getThreadGroups();
		ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
		System.out.println(threadGroup);
		Object controller = threadGroup.getController();
		TestPlanStats stats = null;
		
		if(controller instanceof SimpleController) {
			SimpleController simpleController = (SimpleController) controller;
			System.out.println("RequestId: "+httpRequestId);
			 Summariser summariser = new Summariser("summary");
			HttpRequestDto requestDto = serviceUtil.filterHttpRequestDto(simpleController.getHttpRequestDto(), httpRequestId);
			try {
				System.out.println(requestDto);
//				stats = testPlan(
//			        threadGroup(threadGroup.getNormalProperties().getUsers(), threadGroup.getNormalProperties().getLoopCount(),
//			            httpSampler(requestDto.getBaseUrl()))).run();
				stats  = testPlan(threadGroup().rampTo(200, Duration.ofSeconds(10)).rampToAndHold(100, Duration.ofSeconds(10), Duration.ofSeconds(30)).children(
						httpSampler(requestDto.getBaseUrl())
						)).run();
				
		         
			//PerformanceTestResponse performanceTestResponse = new PerformanceTestResponse();
//				 StatsSummary summary = stats.overall();
//		        String result = String.format(""+
//		            summary.samplesCount()+"  "+
//		         
//		            summary.errorsCount());
				System.out.println("Max"+stats.overall().sampleTime().max());
				System.out.println("Min"+stats.overall().sampleTime().min());
				System.out.println("Mean"+stats.overall().sampleTime().mean());
				System.out.println(stats.overall().sampleTime().perc99());
				System.out.println("Errors:"+stats.overall().errors().total());
				System.out.println("Sample count:"+stats.overall().samples().perSecond());
				System.out.println(stats.overall().sampleTime());
				
			responseDTO.setResponseCode(200);
		//	System.out.println(summary);
			}catch (Exception e) {
				e.printStackTrace();
				responseDTO.setErrorCode(500);
			}
			
		}
		return responseDTO;
	}*/

}

/*	@Override
public ResponseDTO executeHttpRequest(String testPlanId, String threadGroupId, String httpRequestId) {
	ResponseDTO responseDTO = new ResponseDTO();
	ThreadGroupExecutionUtil executionUtil = new ThreadGroupExecutionUtil();
	GenericUtil genericUtil = new GenericUtil();
	ResponseDTO response = testPlanService.getTestPlanById(testPlanId);
	if (response.getErrorCode() != 0) {
		if (response.getErrorCode() == 404) {
			responseDTO.setMessage("Invalid TestPlan Id");
			responseDTO.setErrorCode(404);
		} else {
			responseDTO.setErrorCode(response.getErrorCode());
			responseDTO.setMessage("Couldnot Fetch Test Plan");
		}
	}
	List<ThreadGroup> threadGroups = ((TestPlan) response.getResponseObject()).getThreadGroups();
	ThreadGroup threadGroup = serviceUtil.filterThreadGroup(threadGroups, threadGroupId);
	
	List<ThreadGroupRampAndHoldProperties> rampAndHoldProperties = threadGroup.getThreadGroupRampAndHoldPropertiesList();
	var threadGroupProperties = threadGroup(threadGroup.getThreadGroupName());

	executionUtil.setThreadGroupProperties(threadGroupProperties, threadGroup);
	
	
	
	
	
	
	Object controller = threadGroup.getController();
	// Create a post-processor to capture and print the response body
	String responseData = null;
    DslJsr223PostProcessor postProcessor = jsr223PostProcessor(
        "String responseBody = prev.getResponseDataAsString();" +
        
        		"String responseCodeStr = prev.getResponseCode();\n" + // Get the response code as a String
        	    "int responseCode = Integer.parseInt(responseCodeStr);\n" + // Convert response code to int
        	    "log.info('Response Code: ' + responseCode);\n" + 
        	    "vars.put('responseCode', responseCodeStr);"
    );
  //Json Extractor --PostProcessor
   // DslJsonExtractor jsonExtractor = jsonExtractor("projectId","*.projectId").defaultValue("default");
	TestPlanStats stats = null;
//	var threadGroup = threadGroup();
	
	//Assertions
	 // Add a response assertion to check if the response contains specific text
   // DslResponseAssertion responseAssertion = responseAssertion().containsRegexes("projectName");  
	DslResponseAssertion responseAssertion1 = responseAssertion();
    responseAssertion1.fieldToTest(TargetField.RESPONSE_CODE);
    responseAssertion1.matchesRegexes("201");
    
    DslResponseAssertion responseAssertion2 = responseAssertion();
    responseAssertion2.fieldToTest(TargetField.RESPONSE_MESSAGE);
    responseAssertion2.matchesRegexes("OK");
    
    DslResponseAssertion responseAssertion3 = responseAssertion();
    responseAssertion3.fieldToTest(TargetField.RESPONSE_HEADERS);
    responseAssertion3.matchesRegexes("chunked");
    
 // Create a response assertion
    
//	ResponseAssertion responseAssertion = new ResponseAssertion();
//	responseAssertion.setTestFieldResponseCode();
//    responseAssertion.setToEqualsType();
//    responseAssertion.addTestString("200");
   // responseAssertion.fieldToTest();
    
	//responseAssertion.showInGui();
	
	if(controller instanceof SimpleController) {
		SimpleController simpleController = (SimpleController) controller;
		System.out.println("RequestId: "+httpRequestId);
		 Summariser summariser = new Summariser("summary");
		HttpRequestDto requestDto = serviceUtil.filterHttpRequestDto(simpleController.getHttpRequestDto(), httpRequestId);
		var sampler = httpSampler(requestDto.getBaseUrl());
		String data = "{ \"createdBy\": \"string\",  \"projectName\": \"string\",  \"status\": \"Created\",  \"teamSize\":  \"}";
		//executionUtil.setAssertions(responseAssertion1,requestDto);
		
		
		
		
		
		
		
		//executionUtil.setLiveMonitoring(testPlan,((TestPlan)response.getResponseObject()).getLiveMonitoring());
		try {
			DslTestPlan testPlan = testPlan(csvDataSet("projects.csv"),threadGroupProperties.children(
					sampler,responseAssertion1,jsr223PostProcessor(c -> {
							System.out.println(c.vars.get("${var1}"));
							SampleResult sampleResult  = c.prev;
							String responseHeaders = sampleResult.getResponseHeaders();
							System.out.println("Response Headers: " + responseHeaders);
							sampleResult.setSuccessful(false);
							sampleResult.setResponseMessage("Custom Assertion Failed: Content-Type is not application/json.");
					})
					));
			
			//testPlan.run();
			
			executionUtil.setSimpleController(threadGroupProperties,sampler,simpleController);
			executionUtil.executePreProcessor(testPlan,requestDto.getScripts().getPreProcessor());
			executionUtil.setHeaders(sampler, requestDto);
			executionUtil.setSamplerProperties(sampler, requestDto);
			executionUtil.setDataSet(testPlan, ((TestPlan)response.getResponseObject()).getConfigElement());
			executionUtil.setLiveMonitoring(testPlan, ((TestPlan)response.getResponseObject()).getLiveMonitoring() );
			executionUtil.setReport((TestPlan)response.getResponseObject(),testPlan);
			
			
			
			
			
			
//			if(((TestPlan)response.getResponseObject()).isReport()) {
//				testPlan = testPlan.children(htmlReporter("reports"));
//			}
			stats = testPlan.run();
			
			 //, htmlReporter("reports")
			//System.out.println("Response-Data: "+c.prev.getResponseDataAsString());
			//System.out.println(c.prevMap());
			//	System.out.println("Response-Code: "+c.prev.getResponseCode());
			//setValue(c.prev.getResponseCode());
//			testPlan(threadGroupProperties.children(
//					sampler,postProcessor,responseAssertion
//					)).saveAsJmx("abc.jmx");
		
			  
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		 
}
	return null;
}*/
