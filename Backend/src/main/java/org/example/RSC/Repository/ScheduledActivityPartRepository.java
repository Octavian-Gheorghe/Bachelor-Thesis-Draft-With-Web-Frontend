package org.example.RSC.Repository;

import org.example.RSC.Entity.ScheduledActivityPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledActivityPartRepository extends JpaRepository<ScheduledActivityPart, Integer> {
}
