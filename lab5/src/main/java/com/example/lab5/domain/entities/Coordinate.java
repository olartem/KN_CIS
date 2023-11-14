package com.example.lab5.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coordinate {
    private double latitude;
    private double longitude;
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinate that = (Coordinate) obj;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(latitude) + Double.hashCode(longitude);
    }
}

