package com.backendapi.documents;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document
@Builder
@Data
public class Shop {
    @Id
    private String id;
    private HashMap<String,Integer> evolutionItems;
    private HashMap<String,Integer> consumableItems;
}
