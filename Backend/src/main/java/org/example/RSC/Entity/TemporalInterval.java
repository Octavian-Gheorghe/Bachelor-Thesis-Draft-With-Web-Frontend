package org.example.RSC.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "temporal_intervals")
@Data @NoArgsConstructor @AllArgsConstructor
@Builder
public class TemporalInterval {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "start_interval")
    private LocalDateTime startInterval;
    @Column(name = "end_interval")
    private LocalDateTime endInterval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdea;
}
