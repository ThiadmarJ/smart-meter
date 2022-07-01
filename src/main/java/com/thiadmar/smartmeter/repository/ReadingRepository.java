package com.thiadmar.smartmeter.repository;

import com.thiadmar.smartmeter.model.Reading;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingRepository extends CrudRepository<Reading, Long> {

}
