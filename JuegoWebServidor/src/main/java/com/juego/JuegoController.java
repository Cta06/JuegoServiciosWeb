package com.juego;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/juego")
public class JuegoController {

    private List<String> eventos = Collections.synchronizedList(new ArrayList<>());

    @PostMapping("/evento")
    public void recibirEvento(@RequestBody Evento evento) {

        String msg = evento.getMensaje();

        System.out.println("EVENTO: " + msg);

        eventos.add(msg);

        if (eventos.size() > 20) {
            eventos.clear();
        }
    }

    @GetMapping("/eventos")
    public List<String> obtenerEventos() {

        List<String> copia = new ArrayList<>(eventos);

        eventos.clear();

        return copia;
    }
}