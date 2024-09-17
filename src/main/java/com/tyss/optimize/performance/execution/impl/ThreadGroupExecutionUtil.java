package com.tyss.optimize.performance.execution.impl;


import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tyss.optimize.performance.dto.AssertionRequestDto;
import com.tyss.optimize.performance.dto.BoundaryExtractor;
import com.tyss.optimize.performance.dto.ConfigElement;
import com.tyss.optimize.performance.dto.CsvDataSet;
import com.tyss.optimize.performance.dto.FormData;
import com.tyss.optimize.performance.dto.HttpRequestDto;
import com.tyss.optimize.performance.dto.JsonExtractor;
import com.tyss.optimize.performance.dto.LiveMonitoring;
import com.tyss.optimize.performance.dto.PostProcessor;
import com.tyss.optimize.performance.dto.PreProcessor;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.dto.ThreadGroup;
import com.tyss.optimize.performance.dto.ThreadGroupProperties;
import com.tyss.optimize.performance.dto.ThreadGroupRampAndHoldProperties;
import com.tyss.optimize.performance.dto.controller.Controller;
import com.tyss.optimize.performance.dto.controller.ForEachController;
import com.tyss.optimize.performance.dto.controller.IfController;
import com.tyss.optimize.performance.dto.controller.OnlyOnceController;
import com.tyss.optimize.performance.dto.controller.SimpleController;
import com.tyss.optimize.performance.dto.controller.ThroughputController;
import com.tyss.optimize.performance.dto.controller.TransactionController;
import com.tyss.optimize.performance.dto.controller.WhileController;
import com.tyss.optimize.performance.utils.GenericUtil;

import us.abstracta.jmeter.javadsl.JmeterDsl;
import us.abstracta.jmeter.javadsl.core.DslTestPlan;
import us.abstracta.jmeter.javadsl.core.listeners.GraphiteBackendListener;
import us.abstracta.jmeter.javadsl.core.listeners.InfluxDbBackendListener;
import us.abstracta.jmeter.javadsl.core.threadgroups.DslDefaultThreadGroup;
import us.abstracta.jmeter.javadsl.datadog.DatadogBackendListener;
import us.abstracta.jmeter.javadsl.elasticsearch.listener.ElasticsearchBackendListener;
import us.abstracta.jmeter.javadsl.http.DslHttpSampler;
import us.abstracta.jmeter.javadsl.prometheus.DslPrometheusListener;

@Service
public class ThreadGroupExecutionUtil {
	

	public void setSamplerProperties(DslHttpSampler sampler, HttpRequestDto requestDto) {
		sampler.method(requestDto.getHttpRequestMethod());
		PreProcessor preProcessor = requestDto.getScripts().getPreProcessor();
		if (requestDto.getHttpRequestBody().getRawData() != null
				|| !requestDto.getHttpRequestBody().getRawData().isBlank()) {
			sampler.body(requestDto.getHttpRequestBody().getRawData());
			sampler.header("Content-Type", requestDto.getHttpRequestBody().getRawDataType());
		}
		if (requestDto.getHttpRequestBody().getSetOfFormData().size() != 0) {
			for (FormData formData : requestDto.getHttpRequestBody().getSetOfFormData()) {
				sampler.param(formData.getDataName(), formData.getValue());
			}
		}
		
		if(requestDto.getSettings().isFollowRedirects())
			//sampler.followRedirects(true);
		if(requestDto.getSettings().isUseKeepAlive())
			sampler.header("Connection", "Keep-Alive");
		if(requestDto.getSettings().isRedirectsAutomatically())
			//sampler.header("Redirect", "Auto");
			//sampler.header("redirect", "Follow");
			sampler.header("auto_redirects", "true");
	}

	public void setThreadGroupProperties(DslDefaultThreadGroup threadGroupProperties, ThreadGroup threadGroup,List<DslHttpSampler> samplers) {
		// check for properties i.e Normal or RampAndHold
		if (threadGroup.getThreadGroupProperties().equals(ThreadGroupProperties.NORMAL)) {
			// threadGroupProperties.rampTo(threadGroup.getNormalProperties().getUsers(),
			// Duration.ofSeconds(threadGroup.getNormalProperties().getDurationInSecs())).holdIterating(threadGroup.getNormalProperties().getLoopCount());
			
			threadGroupProperties.rampTo(threadGroup.getNormalProperties().getUsers(), Duration.ofSeconds(threadGroup.getNormalProperties().getDurationInSecs())).holdIterating(threadGroup.getNormalProperties().getLoopCount());

		} else {
			for (ThreadGroupRampAndHoldProperties properties : threadGroup.getThreadGroupRampAndHoldPropertiesList()) {
				threadGroupProperties.rampToAndHold((int) properties.getUsers(),
						Duration.ofSeconds(properties.getRampDuration()),
						Duration.ofSeconds(properties.getHoldDuration()));
			}
		}
//		for(DslHttpSampler sampler : samplers) {
//			threadGroupProperties.children(sampler);
//		}
		
		
	}
	
	
	
	public void setHeaders(DslHttpSampler sampler, HttpRequestDto requestDto) {
		Map<String, String> headers = requestDto.getHeaders();
		Set<String> headerKeys = headers.keySet();
		for(String header : headerKeys) {
			sampler.header(header, headers.get(header));
		}
	}

//	public void setAssertions(DslResponseAssertion assertion, HttpRequestDto requestDto) {
//		for(AssertionRequestDto assert1 :requestDto.getAssertions()) {
//			
//		}
//	}

	public void setPreProcessor(DslHttpSampler sampler,PreProcessor preProcessor) throws Exception {
		GenericUtil genericUtil = new GenericUtil();
		genericUtil.generateRandomCharacters(preProcessor);
		System.out.println("Random String: "+preProcessor.getRandomOutputString().getValue());
		sampler.children(JmeterDsl.jsr223PreProcessor(s -> {
			s.vars.put(preProcessor.getRandomOutputString().getVariableName(), preProcessor.getRandomOutputString().getValue());
		}));
		if(preProcessor.getCustomLogic() != null && !preProcessor.getCustomLogic().isBlank()) {
			String prefix = "import java.util.*; import java.io.*;import org.apache.jmeter.threads.JMeterContextService;import org.apache.jmeter.threads.JMeterVariables;import org.apache.jmeter.util.JMeterUtils;  public class CustomPreProcessorImpl {public void execute() {";
			String suffix = "} }";
			
			DynamicCodeExecutor dynamicCodeExecutor = new DynamicCodeExecutor();
			System.out.println("Code: "+prefix + preProcessor.getCustomLogic() + suffix);
			dynamicCodeExecutor.executeCustomLogic(prefix + preProcessor.getCustomLogic() + suffix);
		}
	}

	public void setLiveMonitoring(DslTestPlan testPlan, LiveMonitoring liveMonitoring) {
		if(liveMonitoring.isPrometheus()) {
			testPlan.children(new DslPrometheusListener());
		}
		if(liveMonitoring.isElasticSearch()) {
			System.out.println("URL: "+liveMonitoring.getElasticSearchUrl());
			testPlan.children(new ElasticsearchBackendListener(liveMonitoring.getElasticSearchUrl()));
			
		}
		if(liveMonitoring.isInfluxdb()) {
			testPlan.children(new InfluxDbBackendListener(liveMonitoring.getInfluxdbUrl()));
		}
		if(liveMonitoring.isGraphite()) {
			testPlan.children(new GraphiteBackendListener(liveMonitoring.getGraphiteUrl()));
		}if(liveMonitoring.isDataDog())
			 testPlan.children(new DatadogBackendListener(liveMonitoring.getDataDogUrl()));
		
	}

	public void setReport(TestPlan responseObject, DslTestPlan testPlan) {
		if(responseObject.isReport())
			testPlan.children(JmeterDsl.htmlReporter(responseObject.getReportPath()));
	}

	public void setDataSet(DslTestPlan testPlan, ConfigElement configElement) {
		File f = new File(configElement.getLocalFilePath());
		testPlan.children(JmeterDsl.csvDataSet(f.getName()));
		System.out.println("File-Name: "+f.getName());
		//testPlan.children(csvDataSet(configElement.getFileDto().getName()+"."+configElement.getFileDto().getType()));
		//testPlan.children(jsr223PreProcessor(s->{System.out.println();}));
	}
	
	public void setSimpleController(DslDefaultThreadGroup threadGroup, DslHttpSampler sampler, SimpleController simpleController) {
		threadGroup.children(JmeterDsl.simpleController(simpleController.getSimpleControllerName()).children(sampler));		
	}
	
	public void setIfController(DslDefaultThreadGroup threadGroup, DslHttpSampler sampler, IfController ifController ) {
		threadGroup.children(JmeterDsl.ifController(ifController.getExpression()).children(sampler));
	}
	
	public void setWhileController(DslDefaultThreadGroup threadGroup, DslHttpSampler sampler, WhileController whileController) {
		threadGroup.children(JmeterDsl.whileController(whileController.getCondition()).children(sampler)); 
	}

	public void setForEachController(DslDefaultThreadGroup threadGroup, DslHttpSampler sampler, ForEachController forEachController) {
		threadGroup.children(JmeterDsl.forEachController(forEachController.getInputVariableName(), forEachController.getOutputVariableName()).children(sampler));
	}
	
	public void setThroughputController(DslDefaultThreadGroup threadGroup, DslHttpSampler sampler, ThroughputController throughputController) {
		threadGroup.children(JmeterDsl.percentController((float)throughputController.getThroughput(),sampler));
	  //Based on total execution is not implemented
	}
	
	public void setTransactionController(DslDefaultThreadGroup threadGroup, DslHttpSampler sampler, TransactionController transactionController) {
		threadGroup.children(JmeterDsl.transaction(transactionController.getTransactionControllerName()).children(sampler));
	}
	
	public void setOnlyOnceController(DslDefaultThreadGroup threadGroup, DslHttpSampler sampler, OnlyOnceController onlyOnceController) {
		threadGroup.children(JmeterDsl.onceOnlyController(sampler));
	}
	
	public void setJsonExtractor(DslHttpSampler sampler, PostProcessor postProcessor) {
		for( JsonExtractor jsonExtractor: postProcessor.getSetOfJsonExtractor()) {
			sampler.children(JmeterDsl.jsonExtractor(jsonExtractor.getVariableName(), jsonExtractor.getJsonPathExpression()).defaultValue(jsonExtractor.getDefaultValue()));
		}	
	}
	
	public void setBoundaryExtractor(DslHttpSampler sampler, PostProcessor postProcessor) {
		for(BoundaryExtractor boundaryExtractor : postProcessor.getSetOfBoundaryExtractor()) {
			sampler.children(JmeterDsl.boundaryExtractor(boundaryExtractor.getVariableName(), boundaryExtractor.getLeftBoundary(), boundaryExtractor.getRightBoundary()).defaultValue(boundaryExtractor.getDefaultValue()));	
		}
	}
	
	public void executePostProcessorCustomLogic(PostProcessor postProcessor) throws Exception {
		String prefix = "import java.util.*; import java.io.*;    public class CustomPreProcessorImpl {public void execute() {";
		String suffix = "} }";
		DynamicCodeExecutor dynamicCodeExecutor = new DynamicCodeExecutor();
		dynamicCodeExecutor.executeCustomLogic(prefix + postProcessor.getCustomLogic() + suffix);
	}
	

/*	public DslTestPlan getTestPlan(String testPlanName) {
		DslTestPlan testPlan = new DslTestPlan(); // do it later after creating ThreadGroup
	}*/

	public DslHttpSampler setControllerProperties(HttpRequestDto requestDto) {
		DslHttpSampler sampler = new DslHttpSampler(requestDto.getHttpRequestName(), requestDto.getBaseUrl());	
		setSamplerProperties(sampler, requestDto);
		return sampler;
	}
	
	public List<HttpRequestDto> getRequestDto(Controller controller) {
		List<HttpRequestDto> requestDtos = null;
		if(controller instanceof SimpleController) {
			SimpleController simpleController = (SimpleController) controller;
			requestDtos  = simpleController.getHttpRequestDto();
		}else if(controller instanceof IfController) {
			IfController ifController = (IfController) controller;
			requestDtos = ifController.getHttpRequestDto();
		}else if(controller instanceof OnlyOnceController) {
			OnlyOnceController onlyOnceController = (OnlyOnceController)controller;
			requestDtos = onlyOnceController.getHttpRequestDto();
		}else if(controller instanceof ForEachController) {
			ForEachController forEachController = (ForEachController)controller;
			requestDtos = forEachController.getHttpRequestDto();
		}else if(controller instanceof WhileController) {
			WhileController whileController = (WhileController)controller;
			requestDtos = whileController.getHttpRequestDto();
		}else if(controller instanceof ThroughputController) {
			ThroughputController throughputController = (ThroughputController) controller;
			requestDtos = throughputController.getHttpRequestDto();
		}else if(controller instanceof TransactionController) {
			TransactionController transactionController = (TransactionController) controller;
			requestDtos = transactionController.getHttpRequestDto();
		}else {
			throw new RuntimeException("Unknown Controller");
		}
		return requestDtos;
	}

	public void setAssertions(DslHttpSampler sampler, List<AssertionRequestDto> assertions) {
		sampler.children(JmeterDsl.jsr223PostProcessor(s ->{
//			System.out.println("ResposneBody: "+s.prev.getResponseDataAsString());
			SampleResult sampleResult = s.prev;
			
			for(AssertionRequestDto assertion : assertions) {
				if(assertion.getIsEnabled()) {
					if(assertion.getLhs().equalsIgnoreCase("statuscode")) {
						setResponseCodeAssertionProperties(assertion, sampleResult);	
					}else if(assertion.getLhs().equalsIgnoreCase("statusmessage")) {
						setStatusMessageAssertionProperties(assertion, sampleResult);
					}else if(assertion.getLhs().equalsIgnoreCase("responsetime")) {
						setResponseTimeAssertionProperties(assertion, sampleResult);
					}else if(assertion.getLhs().equalsIgnoreCase("jsonpath")) {
						setJsonPathAssertionProperties(assertion, sampleResult);
					}else if(assertion.getLhs().equalsIgnoreCase("headerkey")) {
						setHeaderKeyAssertionProperties(assertion, sampleResult);
					}else if(assertion.getLhs().equalsIgnoreCase("responsebody")) {
						setResponseCodeAssertionProperties(assertion, sampleResult);
					}else if(assertion.getLhs().equalsIgnoreCase("contenttype")) {
						setContentTypeAssertionProperties(assertion, sampleResult);
					}else if(assertion.getLhs().equalsIgnoreCase("headervalue")) {
						setHeaderValueAssertionProperties(assertion, sampleResult);
					}
				}
//				if(assertion.getLhs().equalsIgnoreCase("statusmessage")) {
//					if(assertion.getOperator().trim().equals("==")) {
//						if(sampleResult.getResponseMessage().equals(assertion.getRhs())) {
//							sampleResult.setSuccessful(true);
//							sampleResult.setResponseMessage(assertion.getPassMessage());
//						}else {
//							sampleResult.setSuccessful(false);
//							sampleResult.setResponseMessage(assertion.getFailMessage());
//						}
//					}
//				}
			
			}
		}));
		
	}
	public void setContentTypeAssertionProperties(AssertionRequestDto assertion, SampleResult sampleResult) {
		String contentType = sampleResult.getContentType();
		String rhsValue = assertion.getRhs();
		if(assertion.getOperator().trim().equals("==")) {
			if(contentType.equals(rhsValue))
				setAssertionPassed(sampleResult, assertion);
			else
				setAssertionFailed(sampleResult, assertion);
		}else if(assertion.getOperator().trim().equalsIgnoreCase("contains")) {
			if(contentType.contains(rhsValue))
				setAssertionPassed(sampleResult, assertion);
			else
				setAssertionFailed(sampleResult, assertion);
		}else if(assertion.getOperator().trim().equalsIgnoreCase("startswith")) {
			if(contentType.startsWith(rhsValue))
				setAssertionPassed(sampleResult, assertion);
			else
				setAssertionFailed(sampleResult, assertion);
		}else if(assertion.getOperator().trim().equalsIgnoreCase("endswith")) {
			if(contentType.endsWith(rhsValue))
				setAssertionPassed(sampleResult, assertion);
			else
				setAssertionFailed(sampleResult, assertion);
		}
	}
	
	public void setAssertionPassed(SampleResult sampleResult, AssertionRequestDto assertion) {
		sampleResult.setSuccessful(true);
		sampleResult.setResponseMessage(assertion.getPassMessage());
	}
	
	public void setAssertionFailed(SampleResult sampleResult, AssertionRequestDto assertion) {
		sampleResult.setSuccessful(false);
		sampleResult.setResponseMessage(assertion.getFailMessage());
		AssertionResult assertionResult = new AssertionResult(assertion.getName());
		assertionResult.setFailureMessage(assertion.getFailMessage());
		assertionResult.setFailure(true);
		sampleResult.addAssertionResult(assertionResult);
	}
	
	public void setHeaderValueAssertionProperties(AssertionRequestDto assertion, SampleResult sampleResult) {
		String headers = sampleResult.getResponseHeaders();
		HashMap<String, String> headersMap = getHeadersMap(headers);
		Set<String> keys = headersMap.keySet();
		String rhsValue = assertion.getRhs();
		for(String key : keys) {
			if(assertion.getOperator().trim().equals("==")) {
				if(headersMap.get(key).equals(rhsValue))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equalsIgnoreCase("contains")) {
				if(headersMap.get(key).contains(rhsValue))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equalsIgnoreCase("startswith")) {
				if(headersMap.get(key).startsWith(rhsValue))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equalsIgnoreCase("endswith")) {
				if(headersMap.get(key).endsWith(rhsValue))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}
		}
	}
	public void setHeaderKeyAssertionProperties(AssertionRequestDto assertion, SampleResult sampleResult) {
			String headers = sampleResult.getResponseHeaders();
			HashMap<String, String> headersMap = getHeadersMap(headers);
			Set<String> keys = headersMap.keySet();
			String rhsValue = assertion.getRhs();
			for(String key : keys) {
				if(assertion.getOperator().trim().equals("==")) {
					if(key.equals(rhsValue))
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);
				}
				else if(assertion.getOperator().trim().equalsIgnoreCase("contains")) {
					if(key.contains(rhsValue))
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);
				}else if(assertion.getOperator().trim().equalsIgnoreCase("startswith")) {
					if(key.startsWith(rhsValue))
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);
				}else if(assertion.getOperator().trim().equalsIgnoreCase("endswith")) {
					if(key.endsWith(rhsValue))
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);
				}
			}
	}
	
	public HashMap<String,String> getHeadersMap(String headers){
		HashMap<String, String> headersMap = new HashMap<>();
        
        // Split the string into lines
        String[] lines = headers.split("\n");
        
        for (String line : lines) {
            // Split each line into a key-value pair
            String[] header = line.split(":", 2);
            
            if (header.length == 2) {
                String key = header[0].trim();
                String value = header[1].trim();
                headersMap.put(key, value);
            }
        }  
        return headersMap;
	}
	public void setJsonPathAssertionProperties(AssertionRequestDto assertion, SampleResult sampleResult) {
			String responseBody = sampleResult.getResponseDataAsString();
			String rhsValue = assertion.getRhs();
			
			ReadContext context  = JsonPath.parse(responseBody);
			String jsonPathValue = context.read(assertion.getExpression());
			System.out.println("ProjectId: "+jsonPathValue);
			if(assertion.getOperator().trim().equals("==")) {
				if(rhsValue.equals(jsonPathValue))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equalsIgnoreCase("contains")) {
				if(jsonPathValue.contains(rhsValue))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equalsIgnoreCase("startswith")) {
				if(jsonPathValue.startsWith(rhsValue))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equalsIgnoreCase("endswith")) {
				if(jsonPathValue.endsWith(rhsValue))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}	
	}
	public void setResponseTimeAssertionProperties(AssertionRequestDto assertion, SampleResult sampleResult) {
		if(assertion.getIsEnabled()) {
			long responseTime = sampleResult.getTime();
			long rhsValue = Long.parseLong(assertion.getRhs());
			if(assertion.getOperator().trim().equals("==")) {
				if(responseTime == rhsValue)
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}
			else if(assertion.getOperator().trim().equals("<")) {
				if(responseTime < rhsValue)
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}
			else if(assertion.getOperator().trim().equals("<=")) {
				if(responseTime <= rhsValue)
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equals(">")) {
				if(responseTime > rhsValue)
					setAssertionPassed(sampleResult, assertion);
				else 
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equals(">=")) {
				if(responseTime >= rhsValue)
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equals("!=")) {
				if(responseTime != rhsValue)
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else if(assertion.getOperator().trim().equalsIgnoreCase("contains")) {
				String responseTimeString = responseTime+"";
				String RhsValueString = rhsValue+"";
				if(responseTimeString.contains(RhsValueString))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);

			}else if(assertion.getOperator().trim().equalsIgnoreCase("startswith")) {
				String responseTimeString = responseTime+"";
				String RhsValueString = rhsValue+"";
				if(responseTimeString.startsWith(RhsValueString))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}else {
				String responseTimeString = responseTime+"";
				String RhsValueString = rhsValue+"";
				if(responseTimeString.endsWith(RhsValueString))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}
		}
	}

	public void setStatusMessageAssertionProperties(AssertionRequestDto assertion, SampleResult sampleResult) {
		if (assertion.getIsEnabled()) {
			String assertionRhsString = assertion.getRhs();
			String statusMessage = sampleResult.getResponseMessage();

			if (assertion.getOperator().trim().equalsIgnoreCase("contains")) {
				if (statusMessage.contains(assertionRhsString))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);

			} else if (assertion.getOperator().trim().equalsIgnoreCase("notcontains")) {
				if (!statusMessage.contains(assertionRhsString))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);

			} else if (assertion.getOperator().trim().equalsIgnoreCase("startswith")) {
				if (statusMessage.startsWith(assertionRhsString))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			} else {
				if (statusMessage.endsWith(assertionRhsString))
					setAssertionPassed(sampleResult, assertion);
				else
					setAssertionFailed(sampleResult, assertion);
			}
		}
	}

	public void setResponseCodeAssertionProperties(AssertionRequestDto assertion, SampleResult sampleResult) {
		if(assertion.getIsEnabled()) {
			int assertionRhs = Integer.parseInt(assertion.getRhs());
			int statusCode = Integer.parseInt(sampleResult.getResponseCode());
			
				if(assertion.getOperator().trim().equals("==")) {
					if(sampleResult.getResponseCode().equals(assertion.getRhs()))
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);
				}
				else if(assertion.getOperator().trim().equals("<")) {
					if(statusCode < assertionRhs)
						setAssertionPassed(sampleResult, assertion);
					else 
						setAssertionFailed(sampleResult, assertion);	
				}
				else if(assertion.getOperator().trim().equals("<=")) {
					if(statusCode <= assertionRhs)
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);

				}else if(assertion.getOperator().trim().equals(">")) {
					if(statusCode > assertionRhs)
						setAssertionPassed(sampleResult, assertion);
					else 
						setAssertionFailed(sampleResult, assertion);
					
				}else if(assertion.getOperator().trim().equals(">=")) {
					if(statusCode >= assertionRhs)
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);

				}else if(assertion.getOperator().trim().equals("!=")) {
					if(statusCode != assertionRhs)
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);

				}else if(assertion.getOperator().trim().equalsIgnoreCase("contains")) {
					String statusCodeString = statusCode+"";
					String assertionRhsString = assertionRhs+"";
					if(statusCodeString.contains(assertionRhsString)) 
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);
				}else if(assertion.getOperator().trim().equalsIgnoreCase("startswith")) {
					String statusCodeString = statusCode+"";
					String assertionRhsString = assertionRhs+"";
					if (statusCodeString.startsWith(assertionRhsString))
						setAssertionPassed(sampleResult, assertion);
					else 
						setAssertionFailed(sampleResult, assertion);
				}else {
					String statusCodeString = statusCode+"";
					String assertionRhsString = assertionRhs+"";
					if(statusCodeString.endsWith(assertionRhsString)) 
						setAssertionPassed(sampleResult, assertion);
					else
						setAssertionFailed(sampleResult, assertion);
				}
		}
	}

	public void setController(DslDefaultThreadGroup dslThreadGroup, Controller controller, DslHttpSampler sampler) {
		if(controller instanceof WhileController) {
			WhileController whileController = (WhileController) controller;
			dslThreadGroup.children(JmeterDsl.whileController(  whileController.getCondition(), sampler));
		}else if(controller instanceof ForEachController) {
			ForEachController forEachController = (ForEachController) controller;
			//need to modify later(Get Clarity with respect to start index and end index)
			dslThreadGroup.children(JmeterDsl.forEachController(forEachController.getForEachControllerName(),forEachController.getInputVariableName(), forEachController.getOutputVariableName()).children(sampler));		
		
		}else if(controller instanceof ThroughputController) {
			ThroughputController throughputController = (ThroughputController) controller;
			dslThreadGroup.children(JmeterDsl.percentController(throughputController.getThroughput()).children(sampler));
			
		}else if(controller instanceof IfController) {
			IfController ifController = (IfController) controller;
			dslThreadGroup.children(
				   JmeterDsl.ifController(ifController.getExpression())
				        .children(sampler)
				);
		}else if(controller instanceof SimpleController) {
			SimpleController simpleController = (SimpleController) controller;
			dslThreadGroup.children(JmeterDsl.simpleController(simpleController.getSimpleControllerName(), sampler));
		}else if(controller instanceof OnlyOnceController) {
			//OnlyOnceController onlyOnceController = (OnlyOnceController) controller;
			dslThreadGroup.children(JmeterDsl.onceOnlyController(sampler));
		}else if(controller instanceof TransactionController) {
			TransactionController transactionController = (TransactionController) controller;
			dslThreadGroup.children(JmeterDsl.transaction(transactionController.getTransactionControllerName(), sampler));
		}
	}

	public void setCsvDataSet(DslHttpSampler sampler, CsvDataSet csvDataSet) {
		
		//implementing only for local file Path
		if(csvDataSet != null && csvDataSet.isLocalPath()) {
			File csvFile = new File(csvDataSet.getFilePath());
			sampler.children(JmeterDsl.csvDataSet(csvFile.getName()));
		}	
	}
	public void setCsvDataSet(DslDefaultThreadGroup dslThreadGroup, CsvDataSet csvDataSet) {
	
		//implementing only for local file Path
		if(csvDataSet != null  && csvDataSet.isLocalPath()) {
			File csvFile = new File(csvDataSet.getFilePath());
			dslThreadGroup.children(JmeterDsl.csvDataSet(csvFile.getName()));
		}	
	}
}
