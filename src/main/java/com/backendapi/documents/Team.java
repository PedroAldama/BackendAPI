package com.backendapi.documents;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "teams")
@Builder
@Data
public class Team {
    @Id
    private String id;
    private String name;
    private Set<Long> idPokemon;
}
