package com.backendapi.services.auth;

import com.backendapi.configuration.security.JwtTokenProvider;
import com.backendapi.dto.requestdto.UserCredentialRequest;
import com.backendapi.dto.requestdto.UserRegisterRequest;
import com.backendapi.dto.responsedto.DTOUserResponse;
import com.backendapi.entities.Users;
import com.backendapi.entities.users.ERole;
import com.backendapi.entities.users.Role;
import com.backendapi.repositories.RoleRepository;
import com.backendapi.repositories.UsersRepository;
import com.backendapi.services.bag.BagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final BagService bagService;

    @Transactional
    @Override
    public String login(UserCredentialRequest userCredentialRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentialRequest.getUsername(), userCredentialRequest.getPassword())
        );

        //Esto nos permite saber el nombre del usuario logeado, con esto evitamos que nos pase su nombre
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Genera el token
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    @Transactional
    public DTOUserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByName(userRegisterRequest.getUsername()) || userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new IllegalArgumentException("username or email address already in use");
        }
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(
                ()->new IllegalArgumentException("Role Not Found"));

        Users user = Users.builder().email(userRegisterRequest.getEmail())
                .name(userRegisterRequest.getUsername())
                .password(encoder.encode(userRegisterRequest.getPassword()))
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);
        bagService.createBag(user.getName());
        return DTOUserResponse.builder().username(user.getName()).email(user.getEmail()).build();
    }
}
