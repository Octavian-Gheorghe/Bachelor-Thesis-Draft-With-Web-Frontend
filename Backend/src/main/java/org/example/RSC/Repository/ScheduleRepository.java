package org.example.RSC.Repository;

import org.example.RSC.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByUser_Id(Integer userId);
}