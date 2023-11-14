package com.example.lab5.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackName;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrackPoint> trackPoints;

    public double calculateSimilarity(Track otherTrack, int depth) {
        List<TrackPoint> thisTrackPoints = this.getTrackPoints();
        List<TrackPoint> otherTrackPoints = otherTrack.getTrackPoints();

        List<String> thisTrackSequences = new ArrayList<>();
        List<String> otherTrackSequences = new ArrayList<>();

        for (TrackPoint point : thisTrackPoints) {
            thisTrackSequences.add(point.getSequence().substring(0, depth));
        }

        for (TrackPoint point : otherTrackPoints) {
            otherTrackSequences.add(point.getSequence().substring(0, depth));
        }
        thisTrackSequences.retainAll(otherTrackSequences);

        return (double) thisTrackSequences.size() / otherTrackSequences.size();
    }

}

