package com.example.lab3.services;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import com.example.lab3.entities.DataPoint;
import com.example.lab3.repositories.DataPointRepository;
import org.springframework.stereotype.Service;

@Service
public class DataPointService {
    private final DataPointRepository dataPointRepository;

    public DataPointService(DataPointRepository dataPointRepository) {
        this.dataPointRepository = dataPointRepository;
    }

    public void addDataPoint(double x, double y) {
        DataPoint dataPoint = new DataPoint();
        dataPoint.setX(x);
        dataPoint.setY(y);
        dataPointRepository.save(dataPoint);
    }
    public void deleteAll() {
        dataPointRepository.deleteAll();
    }

    public Iterable<DataPoint> getAllDataPoints() {
        return dataPointRepository.findAll();
    }

    public double predictValue(double x) {
        Iterable<DataPoint> dataPoints = getAllDataPoints();

        SimpleRegression regression = new SimpleRegression();
        dataPoints.forEach(point -> regression.addData(point.getX(), point.getY()));

        return regression.predict(x);
    }
}