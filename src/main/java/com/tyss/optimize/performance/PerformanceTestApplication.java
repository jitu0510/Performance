package com.tyss.optimize.performance;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

import us.abstracta.jmeter.javadsl.core.DslTestPlan;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.core.threadgroups.DslThreadGroup;
import us.abstracta.jmeter.javadsl.http.DslHttpSampler;

@SpringBootApplication
public class PerformanceTestApplication {

	public static void main(String[] args) {

		SpringApplication.run(PerformanceTestApplication.class, args);
		
	}
	
	


}

/*
public static void abc() throws IOException {
		TestPlanStats stats = testPlan(
		        threadGroup(2, 10,
		            httpSampler("http://my.service")
		        ),
		        jtlWriter("target/jtls")
		    ).run();
	}
*/