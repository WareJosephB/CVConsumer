package com.qa.CVConsumer.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qa.CVConsumer.persistence.domain.CV;

public interface CVRepository extends MongoRepository<CV, Long> {

}