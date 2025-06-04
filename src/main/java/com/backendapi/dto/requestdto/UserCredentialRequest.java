package com.backendapi.dto.requestdto;

import lombok.Builder;

@Builder
public class UserCredentialRequest {
    private String email;
    private String password;
}
