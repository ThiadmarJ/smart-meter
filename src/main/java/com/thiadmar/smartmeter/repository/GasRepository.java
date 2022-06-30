package com.thiadmar.smartmeter.repository;

import com.thiadmar.smartmeter.model.GasReading;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GasRepository extends CrudRepository<GasReading, Long> {
}
