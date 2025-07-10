package org.example.RSC.Repository;

import org.example.RSC.Entity.ActivityIdea;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityIdeaRepository extends JpaRepository<ActivityIdea, Integer> {
    List<ActivityIdea> findByUser_Id(Integer userId);
    @EntityGraph(attributePaths = {
            "locations",
            "temporalIntervals",
            "activitiesDistanceConstraintsAsFirst",
            "activitiesDistanceConstraintsAsSecond",
            "orderingConstraintsBefore",
            "orderingConstraintsAfter",
            "activityPartDistanceConstraint",
            "activityScheduleTimePreferences",
            "activityPartDistancePreferences",
            "activityDurationPreferences",
            "activitiesDistancePreferencesAsFirst",
            "activitiesDistancePreferencesAsSecond",
            "orderingPreferencesBefore",
            "orderingPreferencesAfter",
            "implicationPreferencesAsInitial",
            "implicationPreferencesAsImplied"
    })
    Optional<ActivityIdea> findDetailedById(Integer id);
}