package com.backendapi.dto.responsedto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DTOPokemonUserListResponse {
    private List<DTOPokemonUserResponse> dtoPokemonUserResponseList;
}
