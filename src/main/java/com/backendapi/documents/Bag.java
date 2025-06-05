package com.backendapi.documents;


import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document(collection = "bags")
@Builder
@Data
public class Bag {
    @Id
    private String id;
    private long money;
    private HashMap<String, Integer> consumableItems;
    private HashMap<String, Integer> evolutionItems;
}
