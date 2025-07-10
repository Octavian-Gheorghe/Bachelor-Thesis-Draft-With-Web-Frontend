package org.example.RSC.Entity.Preference;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.RSC.Entity.ActivityIdea;

@Entity
@Table(name = "ordering_preferences")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderingPreference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_before_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdeaBefore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_after_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdeaAfter;
}
