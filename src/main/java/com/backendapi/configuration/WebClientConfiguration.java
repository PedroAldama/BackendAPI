package com.backendapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    /*Cree un Bean de webclient para poder usarlo en las consultas a la PokeAPI
      le asigne el baseUrl de pokeapi para no tener que repetirlo en cada consulta
    */
    @Bean
    public WebClient webClient(){
        return WebClient.builder().baseUrl("https://pokeapi.co/api/v2/")
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize( 1024 * 1024 / 2))
                .build();
    }
}
