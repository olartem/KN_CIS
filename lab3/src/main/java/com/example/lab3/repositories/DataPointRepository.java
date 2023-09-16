package com.example.lab3.repositories;

import com.example.lab3.entities.DataPoint;
import org.springframework.data.repository.CrudRepository;

public interface DataPointRepository extends CrudRepository<DataPoint, Long> {
}
