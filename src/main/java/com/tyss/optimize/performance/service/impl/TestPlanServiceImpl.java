package com.tyss.optimize.performance.service.impl;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyss.optimize.performance.dto.ResponseDTO;
import com.tyss.optimize.performance.dto.TestPlan;
import com.tyss.optimize.performance.repository.PerformanceRepository;
import com.tyss.optimize.performance.service.TestPlanService;

@Service
public class TestPlanServiceImpl implements TestPlanService{
	
    @Autowired
    private PerformanceRepository performanceRepository;
    
    @Autowired
    private PerformanceServiceUtil performanceServiceUtil;

    @Override
    public ResponseDTO saveTestPlan(TestPlan testPlan) {
        if(testPlan.getId() == null){
            testPlan.setId(UUID.randomUUID().toString());
        }
        ResponseDTO responseDTO = new ResponseDTO();
        TestPlan savedTestPlan = performanceRepository.save(testPlan);
        if(savedTestPlan != null){
            responseDTO.setResponseObject(savedTestPlan);
        }
        else{
            responseDTO.setResponseObject(null);
        }
        return responseDTO;
    }
    
    @Override
    public ResponseDTO getTestPlanById(String id) {
        Optional<TestPlan> optionalTestPlan = performanceRepository.findById(id);
        ResponseDTO responseDTO = new ResponseDTO();
        if(optionalTestPlan.isEmpty()){
            responseDTO.setResponseCode(404);
            responseDTO.setResponseObject(null);
        }else{
            responseDTO.setResponseCode(200);
            responseDTO.setResponseObject(optionalTestPlan.get());
        }
        return responseDTO;
    }
    
    @Override
    public ResponseDTO updateTestPlan(TestPlan testPlan) {
        Optional<TestPlan> optionalTestPlan = performanceRepository.findById(testPlan.getId());
        ResponseDTO responseDTO = new ResponseDTO();
        if (optionalTestPlan.isPresent()) {
            TestPlan existingTestPlan = optionalTestPlan.get();

            if (testPlan.getTestPlanName() != null) {
                existingTestPlan.setTestPlanName(testPlan.getTestPlanName());
            }

            if (testPlan.getConfigElement() != null) {
                if (testPlan.getConfigElement().getTestData() != null) {
                    existingTestPlan.getConfigElement().setFileDto(testPlan.getConfigElement().getFileDto());
                } else if (testPlan.getConfigElement().getLocalFilePath() != null) {
                    existingTestPlan.getConfigElement().setLocalFilePath(testPlan.getConfigElement().getLocalFilePath());
                }

                existingTestPlan.getConfigElement().setHttpCookieManager(testPlan.getConfigElement().isHttpCookieManager());
                existingTestPlan.getConfigElement().setHttpCacheManager(testPlan.getConfigElement().isHttpCacheManager());
            }

            if (testPlan.getLiveMonitoring() != null) {
                existingTestPlan.getLiveMonitoring().setViewTree(testPlan.getLiveMonitoring().isViewTree());
                existingTestPlan.getLiveMonitoring().setDashboardVisualization(testPlan.getLiveMonitoring().isDashboardVisualization());

                updateMonitoringUrl(existingTestPlan.getLiveMonitoring()::setPrometheus, 
                                    existingTestPlan.getLiveMonitoring()::getPrometheusUrl, 
                                    testPlan.getLiveMonitoring()::getPrometheusUrl, 
                                    testPlan.getLiveMonitoring().isPrometheus());

                updateMonitoringUrl(existingTestPlan.getLiveMonitoring()::setGraphana, 
                                    existingTestPlan.getLiveMonitoring()::getGraphanaUrl, 
                                    testPlan.getLiveMonitoring()::getGraphanaUrl, 
                                    testPlan.getLiveMonitoring().isGraphana());

                updateMonitoringUrl(existingTestPlan.getLiveMonitoring()::setGraphite, 
                                    existingTestPlan.getLiveMonitoring()::getGraphiteUrl, 
                                    testPlan.getLiveMonitoring()::getGraphiteUrl, 
                                    testPlan.getLiveMonitoring().isGraphite());

                updateMonitoringUrl(existingTestPlan.getLiveMonitoring()::setInfluxdb, 
                                    existingTestPlan.getLiveMonitoring()::getInfluxdbUrl, 
                                    testPlan.getLiveMonitoring()::getInfluxdbUrl, 
                                    testPlan.getLiveMonitoring().isInfluxdb());

                updateMonitoringUrl(existingTestPlan.getLiveMonitoring()::setElasticSearch, 
                                    existingTestPlan.getLiveMonitoring()::getElasticSearchUrl, 
                                    testPlan.getLiveMonitoring()::getElasticSearchUrl, 
                                    testPlan.getLiveMonitoring().isElasticSearch());

                updateMonitoringUrl(existingTestPlan.getLiveMonitoring()::setDataDog, 
                                    existingTestPlan.getLiveMonitoring()::getDataDogUrl, 
                                    testPlan.getLiveMonitoring()::getDataDogUrl, 
                                    testPlan.getLiveMonitoring().isDataDog());
            }

            if (testPlan.isReport()) {
                if (existingTestPlan.getReportPath() == null || !existingTestPlan.getReportPath().equals(testPlan.getReportPath())) {
                    existingTestPlan.setReport(true);
                    existingTestPlan.setReportPath(testPlan.getReportPath());
                }
            }

            existingTestPlan = performanceRepository.save(existingTestPlan);
            responseDTO.setResponseObject(existingTestPlan);
            responseDTO.setResponseCode(200);
            responseDTO.setMessage("Test Plan updated successfully");

        } else {
            responseDTO.setMessage("Test Plan with testId " + testPlan.getId() + " not found");
            responseDTO.setResponseObject(null);
            responseDTO.setResponseCode(404);
        }

        return responseDTO;
    }

    private void updateMonitoringUrl(Consumer<Boolean> setMonitor, Supplier<String> getExistingUrl, Supplier<String> getNewUrl, boolean isMonitoring) {
        if (isMonitoring) {
            if (getExistingUrl.get() == null || !getExistingUrl.get().equals(getNewUrl.get())) {
                setMonitor.accept(true);
            }
        } else {
            setMonitor.accept(false);
        }
    }

}
