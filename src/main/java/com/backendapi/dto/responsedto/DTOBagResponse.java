package com.backendapi.dto.responsedto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@Builder
public class DTOBagResponse {
    private long money;
    private HashMap<String,Integer> evolutionItems;
    private HashMap<String,Integer> consumableItems;
}
