package org.example.RSC.Entity.Preference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Enum.DistanceType;

@Entity
@Table(name = "activities_distance_preferences")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivitiesDistancePreference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_1_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdea1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_2_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdea2;

    @Column(nullable = false)
    private Integer distance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private DistanceType type;
}
