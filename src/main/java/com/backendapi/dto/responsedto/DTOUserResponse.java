package com.backendapi.dto.responsedto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DTOUserResponse {
    private String username;
    private String email;
}
