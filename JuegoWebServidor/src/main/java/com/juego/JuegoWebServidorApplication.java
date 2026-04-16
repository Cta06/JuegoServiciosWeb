package com.juego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JuegoWebServidorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JuegoWebServidorApplication.class, args);
        System.out.println("SERVIDOR WEB INICIADO");
    }
}