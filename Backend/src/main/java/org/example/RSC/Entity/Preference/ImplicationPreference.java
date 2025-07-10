package org.example.RSC.Entity.Preference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.RSC.Entity.ActivityIdea;

@Entity
@Table(name = "implication_preferences")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ImplicationPreference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_initial_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdeaInitial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_implied_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdeaImplied;
}