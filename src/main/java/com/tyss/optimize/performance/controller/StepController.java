package com.tyss.optimize.performance.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tyss.optimize.performance.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping(value = "/apis")
@Slf4j
public class StepController {
	
//	@Autowired
//	private StepService stepService;
	
	 @PostMapping(value="/step")
	 public ResponseDTO saveAsStep(@RequestParam(required = false) String moduleId, @RequestParam(required = false) String scriptId, @RequestParam String projectId, @RequestPart String stepInputs, @RequestPart(required = false) String optionalParam, @RequestParam(required = false) String libraryId, @RequestParam(required = false) String stepGroupId) throws JsonProcessingException {
	        //return stepService.saveAsStep(moduleId, scriptId, projectId, stepInputs, optionalParam, libraryId, stepGroupId);
		 return null;
	 }

}
