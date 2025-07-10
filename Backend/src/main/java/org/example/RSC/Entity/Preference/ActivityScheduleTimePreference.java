package org.example.RSC.Entity.Preference;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Enum.TimePreferenceType;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_schedule_time_preferences")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivityScheduleTimePreference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdea;

    @Column(name = "time_of_analysis", nullable = false)
    private LocalDateTime timeOfAnalysis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private TimePreferenceType type;
}