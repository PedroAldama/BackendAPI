package com.backendapi.repositories;

import com.backendapi.documents.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShopRepository extends MongoRepository<Shop, Long> {
}
