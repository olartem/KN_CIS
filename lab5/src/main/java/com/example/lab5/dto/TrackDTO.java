package com.example.lab5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrackDTO {
    private String trackName;

    private double similarity;
}
