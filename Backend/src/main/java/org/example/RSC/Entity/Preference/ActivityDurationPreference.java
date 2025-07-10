package org.example.RSC.Entity.Preference;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.RSC.Entity.ActivityIdea;

@Entity
@Table(name = "activity_duration_preferences")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivityDurationPreference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdea;

    @Column(name = "minimum_duration", nullable = false)
    private Integer minimumDuration;

    @Column(name = "maximum_duration", nullable = false)
    private Integer maximumDuration;
}