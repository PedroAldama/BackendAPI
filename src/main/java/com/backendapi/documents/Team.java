package com.backendapi.documents;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
/**
 * @author Pedro Aldama
 * Un equipo puede tener hasta un maximo de 6 miembros
 * Cada usuario solo puede tener un equipo pero sus pokemon pueden ir cambiando
 */

@Document(collection = "teams")
@Builder
@Data
public class Team {
    @Id
    private String id;
    private String name;
    private Set<Long> idPokemon;
}
