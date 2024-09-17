package com.tyss.optimize.performance.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyss.optimize.performance.dto.HttpRequestDto;
import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.dto.ThreadGroup;
import com.tyss.optimize.performance.dto.ThreadGroupNormalProperties;
import com.tyss.optimize.performance.dto.controller.ForEachController;
import com.tyss.optimize.performance.dto.controller.IfController;
import com.tyss.optimize.performance.dto.controller.OnlyOnceController;
import com.tyss.optimize.performance.dto.controller.SimpleController;
import com.tyss.optimize.performance.dto.controller.ThroughputController;
import com.tyss.optimize.performance.dto.controller.TransactionController;
import com.tyss.optimize.performance.dto.controller.WhileController;
import com.tyss.optimize.performance.repository.PerformanceRepository;
import com.tyss.optimize.performance.utils.GenericUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PerformanceServiceImpl {



  

   




  

  

    
    public ResponseDTO updateLogicController(Object logicController, String testPlanId, String threadGroupId) {
      /*  Optional<TestPlan> optionalTestPlan = performanceRepository.findById(testPlanId);
        ResponseDTO responseDTO = new ResponseDTO();
        TestPlan testPlan = optionalTestPlan.get();
        if(testPlan != null){
            List<ThreadGroup> threadGroups = testPlan.getThreadGroups();
            ThreadGroup threadGroup = threadGroups.stream().filter(tg -> tg.getThreadGroupId().equals(threadGroupId)).findFirst().get();
//            if(logicController instanceof SimpleController){
//                SimpleController simpleControllerEntity = (SimpleController) threadGroup.getController();
//                SimpleController simpleController = (SimpleController) logicController;
//                simpleControllerEntity.setSimpleControllerName(simpleController.getSimpleControllerName());
//                threadGroup.setController(simpleControllerEntity);
//            }else
//            	if(logicController instanceof OnlyOnceController){
//                OnlyOnceController onlyOnceControllerEntity = (OnlyOnceController) threadGroup.getController();
//                OnlyOnceController onlyOnceController = (OnlyOnceController) logicController;
//                onlyOnceControllerEntity.setOnlyOnceControllerName(onlyOnceController.getOnlyOnceControllerName());
//                threadGroup.setController(onlyOnceControllerEntity);
//            }else if(logicController instanceof TransactionController){
//                TransactionController transactionControllerEntity = (TransactionController) threadGroup.getController();
//                TransactionController transactionController = (TransactionController) logicController;
//                transactionControllerEntity.setTransactionControllerName(transactionController.getTransactionControllerName());
//                threadGroup.setController(transactionControllerEntity);
//            }else if(logicController instanceof IfController){
//                IfController ifControllerEntity = (IfController) threadGroup.getController();
//                IfController ifController = (IfController) logicController;
//                ifControllerEntity.setIfControllerName(ifController.getIfControllerName());
//                ifControllerEntity.setExpression(ifController.getExpression());
//                threadGroup.setController(ifControllerEntity);
//            }else if(logicController instanceof ThroughputController){
//                ThroughputController throughputControllerEntity = (ThroughputController) threadGroup.getController();
//                ThroughputController throughputController = (ThroughputController) logicController;
//                throughputControllerEntity.setThroughputControllerName(throughputController.getThroughputControllerName());
//                throughputControllerEntity.setThroughput(throughputController.getThroughput());
//                throughputControllerEntity.setBasedOn(throughputController.getBasedOn());
//                threadGroup.setController(throughputControllerEntity);
//            }else if(logicController instanceof WhileController){
//                WhileController whileControllerEntity = (WhileController) threadGroup.getController();
//                WhileController whileController = (WhileController) logicController;
//                whileControllerEntity.setWhileControllerName(whileController.getWhileControllerName());
//                whileControllerEntity.setCondition(whileController.getCondition());
//                threadGroup.setController(whileControllerEntity);
//            }else if(logicController instanceof ForEachController){
//                ForEachController forEachControllerEntity = (ForEachController) threadGroup.getController();
//                ForEachController forEachController = (ForEachController) logicController;
//                forEachControllerEntity.setForEachControllerName(forEachController.getForEachControllerName());
//                forEachControllerEntity.setInputVariableName(forEachController.getInputVariableName());
//                forEachControllerEntity.setStartIndex(forEachController.getStartIndex());
//                forEachControllerEntity.setEndIndex(forEachController.getEndIndex());
//                forEachControllerEntity.setOutputVariableName(forEachController.getOutputVariableName());
//                threadGroup.setController(forEachControllerEntity);
//            }else{
//                responseDTO.setMessage("Exception While Parsing Controller");
//                responseDTO.setResponseObject(null);
//                responseDTO.setResponseCode(500);
//                return responseDTO;
//            }
//            threadGroups.set(threadGroups.indexOf(threadGroup),threadGroup);
//            testPlan.setThreadGroups(threadGroups);
//            responseDTO.setResponseObject(performanceRepository.save(testPlan));
//            responseDTO.setResponseCode(200);
//            responseDTO.setMessage("Logic Controller is Updated");
//            return responseDTO;
//        }
//        responseDTO.setResponseObject(null);
//        responseDTO.setMessage("Test Plan is Empty");
//        responseDTO.setResponseCode(500);*/
        return null;
    }


	
	
}
