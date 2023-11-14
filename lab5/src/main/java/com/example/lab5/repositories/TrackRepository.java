package com.example.lab5.repositories;

import com.example.lab5.domain.entities.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
