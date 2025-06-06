package com.backendapi.controllers;

import com.backendapi.dto.requestdto.UserCredentialRequest;
import com.backendapi.dto.requestdto.UserRegisterRequest;
import com.backendapi.dto.responsedto.DTOUserJWTResponse;
import com.backendapi.dto.responsedto.DTOUserResponse;
import com.backendapi.services.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<DTOUserJWTResponse> login(@RequestBody UserCredentialRequest loginRequest) {
        String token = authService.login(loginRequest);
        DTOUserJWTResponse response = DTOUserJWTResponse.builder()
                .token(token)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    public ResponseEntity<DTOUserResponse> register(@RequestBody UserRegisterRequest userRegister) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userRegister));
    }
}
