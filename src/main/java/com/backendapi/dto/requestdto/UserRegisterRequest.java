package com.backendapi.dto.requestdto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserRegisterRequest {
    private String userName;
    private String password;
    private String email;
}
