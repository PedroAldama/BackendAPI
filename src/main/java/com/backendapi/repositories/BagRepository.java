package com.backendapi.repositories;

import com.backendapi.documents.Bag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BagRepository extends MongoRepository<Bag, String> {
}
