package com.tyss.optimize.performance.repository;

import com.tyss.optimize.performance.dto.TestPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PerformanceRepository extends MongoRepository<TestPlan, String> {
}
