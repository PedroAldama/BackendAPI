package com.backendapi.dto.responsedto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DTOUserJWTResponse {
    private String token;
}
