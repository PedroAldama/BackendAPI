package com.backendapi.repositories;

import com.backendapi.documents.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishListRepository extends MongoRepository<WishList, String> {
}
