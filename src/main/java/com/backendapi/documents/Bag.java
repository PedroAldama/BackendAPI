package com.backendapi.documents;


import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
/**
 * @author Pedro Aldama
 * Documento de Mongo para guardar los objetos que vayamos comprando
 * Cada vez que compramos o ganamos monedas, money cambia su valor
 * Los items se consumen si los usas y puedes agregar mas comprandolos en la tienda
*/

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
