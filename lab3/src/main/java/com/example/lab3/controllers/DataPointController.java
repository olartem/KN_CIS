package com.example.lab3.controllers;

import com.example.lab3.entities.DataPoint;
import com.example.lab3.services.DataPointService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataPointController {
    private final DataPointService dataPointService;

    public DataPointController(DataPointService dataPointService) {
        this.dataPointService = dataPointService;
    }

    @PostMapping("/add")
    public void addDataPoints(@RequestBody List<List<Double>> dataPoints) {
        dataPointService.deleteAll();
        for (List<Double> point : dataPoints) {
            if (point.size() == 2) {
                double x = point.get(0);
                double y = point.get(1);
                dataPointService.addDataPoint(x, y);
            }
        }
    }

    @GetMapping
    public Iterable<DataPoint> getAllDataPoints() {
        return dataPointService.getAllDataPoints();
    }

    @GetMapping("/predict")
    public double predictValue(@RequestParam double x) {
        return dataPointService.predictValue(x);
    }
}