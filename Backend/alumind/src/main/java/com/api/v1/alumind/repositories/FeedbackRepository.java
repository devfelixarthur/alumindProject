package com.api.v1.alumind.repositories;

import com.api.v1.alumind.entities.Feedback;
import com.api.v1.alumind.enums.Sentiment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByDtRegisterBetween(LocalDateTime dtStart, LocalDateTime dtEnd);

    @Query("SELECT f FROM Feedback f WHERE " +
            "(:id IS NULL OR f.id = :id) AND " +
            "(:sentiment IS NULL OR f.sentiment = :sentiment) AND " +
            "(f.dtRegister BETWEEN COALESCE(:startDate, f.dtRegister) AND COALESCE(:endDate, f.dtRegister))")
    Page<Feedback> findFeedbacksByIdAndSentiment(
            @Param("id") Long id,
            @Param("sentiment") Sentiment sentiment,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );



}
