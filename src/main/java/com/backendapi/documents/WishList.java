package com.backendapi.documents;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "wishlists")
@Builder
@Data
public class WishList {
    @Id
    private String idUser;
    private Set<String> evolutionItems;
}
