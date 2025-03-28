package com.api.v1.alumind.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Column(name = "dt_register", updatable = false, insertable = true)
    private LocalDateTime dtRegister;

}
