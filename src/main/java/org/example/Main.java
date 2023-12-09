package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.UsuarioController;
import org.example.service.UsuarioService;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        UsuarioService usuarioService = new UsuarioService();
        ObjectMapper objectMapper = new ObjectMapper();
        UsuarioController usuarioController = new UsuarioController(usuarioService, objectMapper);

        Spark.port(8080);

        Spark.before((request, response) -> response.type("application/json"));

        Spark.path("/api", usuarioController::respostasRequisicoes);
    }
}