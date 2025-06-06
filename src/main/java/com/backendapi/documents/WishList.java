package com.backendapi.documents;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
/**
 * @author Pedro Aldama
 * Lista donde puedes guardar o eliminar objetos deseados

 */
@Document(collection = "wishlists")
@Builder
@Data
public class WishList {
    @Id
    private String id;
    private Set<String> evolutionItems;
}
