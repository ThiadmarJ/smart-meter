package com.thiadmar.smartmeter.repository;

import com.thiadmar.smartmeter.model.ElecReading;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElecRepository extends CrudRepository<ElecReading, Long> {
}
