package com.backendapi.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    //Clase de configuracion para activar Scheduling y poder actualizar la tienda cada dia a las 00:00
}
