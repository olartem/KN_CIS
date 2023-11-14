package com.example.lab5.controllers;

import com.example.lab5.domain.entities.Track;
import com.example.lab5.dto.TrackDTO;
import com.example.lab5.services.TrackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {
    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTrack(@RequestParam("file") MultipartFile file) {
        // Save track to the database
        if(trackService.saveTrack(file)) {
            return ResponseEntity.ok("Track uploaded successfully.");
        }
        else {
            return ResponseEntity.ok("Track is at least 70% duplicate of others(didn't save)");
        }

    }

    @GetMapping
    public ResponseEntity<List<Track>> getAllTracks() {
        List<Track> allTracks = trackService.getAllTracks();
        return ResponseEntity.ok(allTracks);
    }

    @GetMapping("/similar")
    public ResponseEntity<List<TrackDTO>> getSimilarTracks(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("depth") int depth) {
        List<TrackDTO> similarTracks = trackService.getSimilarTracks(file, depth);
        return ResponseEntity.ok(similarTracks);
    }
}
