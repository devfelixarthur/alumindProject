package com.api.v1.alumind.repositories;

import com.api.v1.alumind.entities.RequestedFeature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestedFeatureRepository extends JpaRepository<RequestedFeature, Long> {
    boolean existsByCode(String generatedCode);
}
