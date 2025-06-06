package com.backendapi.services.auth;

import com.backendapi.dto.requestdto.UserCredentialRequest;
import com.backendapi.dto.requestdto.UserRegisterRequest;
import com.backendapi.dto.responsedto.DTOUserResponse;

public interface AuthService {
    String login(UserCredentialRequest userCredentialRequest);
    DTOUserResponse registerUser(UserRegisterRequest userRegisterRequest);
}
