package org.example.RSC.Repository.Preference;

import org.example.RSC.Entity.Preference.ActivityDurationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityDurationPreferenceRepository extends JpaRepository<ActivityDurationPreference, Integer> {}
