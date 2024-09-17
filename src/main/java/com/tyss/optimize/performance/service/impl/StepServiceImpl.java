//package com.tyss.optimize.performance.service.impl;
//
//import java.util.UUID;
//
//import org.apache.commons.lang3.StringUtils;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tyss.optimize.performance.dto.ActionEnum;
//import com.tyss.optimize.performance.dto.ResponseDTO;
//import com.tyss.optimize.performance.dto.ResponseDto;
//import com.tyss.optimize.performance.dto.StepInputs;
//import com.tyss.optimize.performance.dto.TestPlan;
//import com.tyss.optimize.performance.service.StepService;
//
//import lombok.extern.slf4j.Slf4j;
//
//
//
//@Service
//@Slf4j
//public class StepServiceImpl implements StepService{
//	
//	@Autowired
//    @Qualifier("objectMapper")
//    private ObjectMapper objectMapper ;
//
//	@Override
//	public ResponseDto saveAsStep(TestPlan testPlan, String scriptId, String moduleId, String projectId,
//			String stepInputs, String optionalParam, String libraryId, String stepGroupId) {
//		
//		String initialHistoryId = testPlan.getId();
//        String newHistoryId = StringUtils.isBlank(initialHistoryId) ? UUID.randomUUID().toString(): initialHistoryId;
//        log.info("original history id = {}, new history id = {}", initialHistoryId, newHistoryId);
//        // folder structure is create with this newHistoryId, later create history & store in db
//        testPlan.setId(newHistoryId);
//
////        ResponseDTO responseDto = apiService.executeApiRequestForScript(testPlan,projectId);
////        if(responseDto.getErrorCode() != 0)
////        {
////            return new ResponseDto(null, new ErrorResponse(ERROR_CODE, UNABLE_TO_CREATE_STEP, responseDto.getErrors().getDetails()));
////        }
//        return scriptService.getScriptAndUpdateStep(moduleId,scriptId,projectId,responseDto, stepInputs, optionalParam,libraryId,stepGroupId, initialHistoryId);
//
//	}
//	
//	@Override
//    public ResponseDto getScriptAndUpdateStep(String scriptId, String moduleID, String projectId, ResponseDto apiResponseDto, String stepInputs, String optionalParam, String libraryId, String stepGroupId, String initialHistoryId) throws JsonProcessingException {
//        ApiRequestDto apiRequestDto = objectMapper.convertValue(apiResponseDto.getSuccess(), ApiRequestDto.class);
//        StepInputs stInputs = objectMapper.readValue(stepInputs, StepInputs.class);
//        apiRequestDto.setName(stInputs.getName());
//        String newStepId = apiRequestDto.getId();//pass new step id
//
//        JSONObject optionalParamJSON = convertOptional2Json(optionalParam);
//        String action = StringUtils.isBlank(stInputs.getStepId()) ? ActionEnum.ADD.name() : ActionEnum.UPDATE.name();
//
//        boolean isModuleScriptStep = StringUtils.isNotBlank(moduleID);
//
//        if (isModuleScriptStep) {
//            Script script = getScript(moduleID, scriptId);
//            if (script == null) {
//                return new ResponseDto(null, new ErrorResponse(ERROR_CODE, UNABLE_TO_CREATE_STEP, new HashSet<>()));
//            }
//            Script savedScript = handleScriptOperations(projectId, scriptId, moduleID, libraryId, stepGroupId, initialHistoryId, apiRequestDto, stInputs, newStepId, optionalParamJSON, action, script);
//            if (savedScript == null) {
//                return createErrorResponse();
//            }
//        } else {
//            StepGroup stepGroup = getStepGroup(libraryId, stepGroupId);
//            if (stepGroup == null) {
//                return createErrorResponse();
//            }
//            Map<String, String> parameterReference = stepGroup.getParameters().stream().collect(Collectors.toMap(Parameter::getId, Parameter::getName));
//            StepGroup savedStepGroup = handleStepGroupOperations(projectId, scriptId, moduleID, libraryId, stepGroupId, initialHistoryId, apiRequestDto, stInputs, newStepId, optionalParamJSON, action, stepGroup, parameterReference);
//            if (savedStepGroup == null) {
//                return createErrorResponse();
//            }
//        }
//        return new ResponseDto(STEP_CREATED_SUCCESSFULLY, null);
//    }
//	
//	public static JSONObject convertOptional2Json(String optionalParam) {
//        JSONObject optionalJSON = new JSONObject();
//        if (StringUtils.isNotBlank(optionalParam)) {
//            try {
//                JSONParser parser = new JSONParser();
//                optionalJSON = (JSONObject) parser.parse(optionalParam);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return optionalJSON;
//    }
//
//}
