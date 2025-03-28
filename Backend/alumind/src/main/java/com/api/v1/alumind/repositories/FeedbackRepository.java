package com.api.v1.alumind.repositories;

import com.api.v1.alumind.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
