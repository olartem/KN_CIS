package com.example.lab5.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class TrackPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sequence;

    @ManyToOne
    @JoinColumn(name = "track_id")
    @JsonIgnore
    private Track track;

}
