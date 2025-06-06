package com.backendapi.documents;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
/**
 * @author Pedro Aldama
 * Documento de mongo para almacenar los objetos que puedes comprar
 * se actualiza cada dia a las 00:00
 * La tienda es compartida por todos los usuarios
 */
@Document
@Builder
@Data
public class Shop {
    @Id
    private String id;
    private HashMap<String,Integer> evolutionItems;
    private HashMap<String,Integer> consumableItems;
}
