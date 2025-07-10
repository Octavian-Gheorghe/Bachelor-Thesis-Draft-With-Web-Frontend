package org.example.RSC.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.RSC.DTO.LocationDTO;
import org.example.RSC.Entity.ActivityIdea;
import org.example.RSC.Entity.Location;
import org.example.RSC.Repository.ActivityIdeaRepository;
import org.example.RSC.Repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class LocationService {
    private final LocationRepository repo;
    private final ActivityIdeaRepository actRepo;

    public List<Location> findAll()
    {
        return repo.findAll();
    }
    public Location findById(Integer id)
    {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Location "+id+" not found"));
    }

    public Location mapLocationToActivity(Integer locId, Integer actId) {
        ActivityIdea idea = actRepo.findById(actId)
                .orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + actId + " not found"));

        Location loc = repo.findById(locId)
                .orElseThrow(() -> new EntityNotFoundException("Location " + locId + " not found"));

        if (!idea.getLocations().contains(loc)) {
            idea.getLocations().add(loc);
            loc.getActivityIdeas().add(idea);
            actRepo.save(idea);
        }
        return loc;
    }

    public Location unmapLocationToActivity(Integer locId, Integer actId) {
        ActivityIdea idea = actRepo.findById(actId)
                .orElseThrow(() -> new EntityNotFoundException("ActivityIdea " + actId + " not found"));

        Location loc = repo.findById(locId)
                .orElseThrow(() -> new EntityNotFoundException("Location " + locId + " not found"));

        if (idea.getLocations().contains(loc))
        {
            idea.getLocations().remove(loc);
            loc.getActivityIdeas().remove(idea);
            actRepo.save(idea);
        }

        return loc;
    }

    public Location save(LocationDTO dto) {
        List<Location> existing = repo.findAll();

        for (Location loc : existing) {
            if (loc.getName().equalsIgnoreCase(dto.getName())
                    && Float.compare(loc.getX(), dto.getX()) == 0
                    && Float.compare(loc.getY(), dto.getY()) == 0) {
                return loc;
            }
        }

        Location loc = Location.builder()
                .name(dto.getName())
                .x(dto.getX())
                .y(dto.getY())
                .build();

        return repo.save(loc);
    }
    public Location update(Integer id, LocationDTO dto)
    {
        Location db = findById(id);
        db.setName(dto.getName());
        db.setX(dto.getX());
        db.setY(dto.getY());
        return repo.save(db);
    }

    public void delete(Integer id) {
        Location location = findById(id);

        for (ActivityIdea activity : location.getActivityIdeas()) {
            activity.getLocations().remove(location);
        }
        location.getActivityIdeas().clear();

        repo.save(location);
        repo.deleteById(id);
    }
}
