package org.example.RSC.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.RSC.Entity.Constraint.ActivitiesDistanceConstraint;
import org.example.RSC.Entity.Constraint.ActivityPartDistanceConstraint;
import org.example.RSC.Entity.Constraint.OrderingConstraint;
import org.example.RSC.Entity.Preference.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activity_ideas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityIdea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String  name;
    @Column(nullable = false)
    private Integer durimin;
    @Column(nullable = false)
    private Integer durimax;

    private Integer smin;
    private Integer smax;
    private Integer dmin;
    private Integer dmax;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "activity_idea_location",
            joinColumns = @JoinColumn(name = "activity_idea_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @JsonManagedReference
    private List<Location> locations = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdea", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TemporalInterval> temporalIntervals = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdea1", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ActivitiesDistanceConstraint> activitiesDistanceConstraintsAsFirst = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdea2", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ActivitiesDistanceConstraint> activitiesDistanceConstraintsAsSecond = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdeaBefore", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderingConstraint> orderingConstraintsBefore = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdeaAfter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderingConstraint> orderingConstraintsAfter = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdea", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ActivityScheduleTimePreference> activityScheduleTimePreferences = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdea", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ActivityPartDistancePreference> activityPartDistancePreferences = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdea", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ActivityPartDistanceConstraint> activityPartDistanceConstraints = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdea", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ActivityDurationPreference> activityDurationPreferences = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdea1", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ActivitiesDistancePreference> activitiesDistancePreferencesAsFirst = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdea2", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ActivitiesDistancePreference> activitiesDistancePreferencesAsSecond = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdeaBefore", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderingPreference> orderingPreferencesBefore = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdeaAfter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderingPreference> orderingPreferencesAfter = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdeaInitial", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImplicationPreference> implicationPreferencesAsInitial = new ArrayList<>();

    @OneToMany(mappedBy = "activityIdeaImplied", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImplicationPreference> implicationPreferencesAsImplied = new ArrayList<>();

}