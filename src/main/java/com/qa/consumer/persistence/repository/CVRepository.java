package com.qa.consumer.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qa.consumer.persistence.domain.CV;

public interface CVRepository extends MongoRepository<CV, Long> {

}