package com.backendapi.dto.requestdto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserCredentialRequest {
    private String username;
    private String password;
}
