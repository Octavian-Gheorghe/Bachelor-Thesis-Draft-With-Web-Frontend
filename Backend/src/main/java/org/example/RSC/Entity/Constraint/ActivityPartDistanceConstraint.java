package org.example.RSC.Entity.Constraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Enum.DistanceType;

@Entity
@Table(name = "activity_part_distance_constraints")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivityPartDistanceConstraint {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_idea_id", nullable = false)
    @JsonBackReference
    private ActivityIdea activityIdea;

    @Column(nullable = false)
    private Integer distance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private DistanceType type;
}