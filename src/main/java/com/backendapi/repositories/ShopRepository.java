package com.backendapi.repositories;

import com.backendapi.documents.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShopRepository extends MongoRepository<Shop, Long> {
    Optional<Shop> findById(String name);
}
