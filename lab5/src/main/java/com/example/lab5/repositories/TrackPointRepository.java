package com.example.lab5.repositories;

import com.example.lab5.domain.entities.TrackPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackPointRepository extends JpaRepository<TrackPoint, Long> {
}
