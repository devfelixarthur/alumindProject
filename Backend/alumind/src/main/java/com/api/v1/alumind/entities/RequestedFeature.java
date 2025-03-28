package com.api.v1.alumind.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "requested_feature")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestedFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 75, unique = true)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false)
    @JsonBackReference
    private Feedback feedback;

    @Column(name = "dt_register", updatable = false, insertable = true)
    private LocalDateTime dtRegister;
}
