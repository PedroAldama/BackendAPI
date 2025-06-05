package com.backendapi.dto.pokeapi;

import lombok.Data;

import java.util.List;

@Data
public class ItemsResponse {
    private List<Items> items;

        @Data
        public static class Items {
            private String name;
        }

}
