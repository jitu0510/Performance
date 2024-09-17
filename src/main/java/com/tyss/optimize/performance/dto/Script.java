//package com.tyss.optimize.performance.dto;
//
//
//import com.fasterxml.jackson.annotation.JsonAlias;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.tyss.optimize.data.models.dto.results.ExecutionEntityResponse;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Transient;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//import java.util.List;
//import java.util.Map;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Document(value = "scripts")
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class Script extends BaseEntity {
//
//    @Transient
//    public static final String SEQUENCE_NAME = "SCRIPT";
//
//    @JsonAlias("_id")
//    String id;
//    private String key;
//    @NotNull(message = "name is mandatory")
//    @NotBlank(message = "name must not be blank")
//
//    String name;
//
//    @ApiModelProperty(notes = "WEB/Database/Webservice")
//    @NotNull(message = "Script type is mandatory")
//    @NotBlank(message = "script type must not be blank")
//    String scriptType;
//    @Size(max = 200, message = "Description must be 200 characters")
//    String desc;
//    String type;
//    @NotNull(message = "testCaseType is mandatory")
//    @NotBlank(message = "testCaseType must not be blank")
//    String testCaseType;
//    String templateId;
//    double executionOrder;
//    List<Step> steps;
//    int stepCount;
//    int hierarchy;
//    List<String> selectedSystems;
//    String delayBetweenSteps;
//    List<DependentScript> dependentScript;
//    List<Conditions> conditions;
//    Map<String, String> localVariables;
//    List<DataProvider> dataProvider;
//    ExecutionEntityResponse scriptResult;
//    int preConditionCount;
//    int postConditionCount;
//    int dependentScriptCount;
//    String title;
//    Map<String, Object> manualTestCase;
//    private List<ProjectLabel> projectLabels;
//    @Transient
//    private boolean containsAutomationScript;
//
//    //CROWD specific properties
//    private List<String> description;
//    private String summary;
//    private String instruction;
//    private String modules;
//    private List<String> testSteps;
//    private List<String> testData;
//    private List<String> expectedResult;
//    private List<String> actualResult;
//    private List<String> status;
//    private List<Assignee> assignee;
//    private boolean imported = false;
//    private int assigneeCount = 0;
//    private Boolean inactive;
//    private String importId;
//    private ParentLevelAuthorization authorizationInfo;
//    private String prefix;
//}
