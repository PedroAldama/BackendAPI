package com.backendapi.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
/**
 * @author Pedro Aldama
 * Clase estatica para poder obtener el usuario logeado en otras clases
 * Fundamental para que funcione UserPokemonService sin necesidad de ingresar usuario

 */
public class SecurityUtils {
    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
