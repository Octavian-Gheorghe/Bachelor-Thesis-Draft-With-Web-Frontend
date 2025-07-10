package org.example.RSC.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float x;

    @Column(nullable = false)
    private float y;

    @ManyToMany(mappedBy = "locations")
    @JsonBackReference
    private List<ActivityIdea> activityIdeas = new ArrayList<>();
}
