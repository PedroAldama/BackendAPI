package com.backendapi.repositories;

import com.backendapi.documents.Bag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BagRepository extends MongoRepository<Bag, String> {
    Optional<Bag> findById(String name);
}
