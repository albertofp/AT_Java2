package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UsuarioDTOInput;
import org.example.service.UsuarioService;
import spark.Request;
import spark.Response;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.delete;
import static spark.Spark.put;

public class UsuarioController {
    private UsuarioService usuarioService;
    private ObjectMapper objectMapper;

    public UsuarioController(UsuarioService usuarioService, ObjectMapper objectMapper) {
        this.usuarioService = usuarioService;
        this.objectMapper = objectMapper;
    }

    public void respostasRequisicoes() {
        get("/usuarios", (request, response) -> {
            try {
                response.type("application/json");
                response.status(200);
                return objectMapper.writeValueAsString(usuarioService.listar());
            } catch (Exception e) {
                response.status(500);
                return "Erro";
            }
        });

        get("/usuarios/:id", (request, response) -> {
            try {
                int userId = Integer.parseInt(request.params(":id"));
                var usuario = usuarioService.buscar(userId);

                if (usuario != null) {
                    String usuarioJson = objectMapper.writeValueAsString(usuario);
                    response.type("application/json");
                    response.status(200);
                    return usuarioJson;
                } else {
                    response.status(404);
                    return "Usuário não encontrado";
                }
            } catch (Exception e) {
                response.status(500);
                return "Erro";
            }
        });

        delete("/usuarios/:id", (request, response) -> {
            try {
                int userId = Integer.parseInt(request.params(":id"));
                usuarioService.excluir(userId);
                response.status(204);
                return "Usuário excluido com sucesso";
            } catch (Exception e) {
                response.status(500);
                return "Erro";
            }
        });

        post("/usuarios", (request, response) -> {
            try {
                UsuarioDTOInput usuarioDTOInput = objectMapper.readValue(request.body(), UsuarioDTOInput.class);
                usuarioService.inserir(usuarioDTOInput);
                response.status(201);
                return "Usuário inserido com sucesso";
            } catch (Exception e) {
                response.status(500);
                return "Erro";
            }
        });

        put("/usuarios", (request, response) -> {
            try {
                UsuarioDTOInput usuarioDTOInput = objectMapper.readValue(request.body(), UsuarioDTOInput.class);
                usuarioService.alterar(usuarioDTOInput);
                response.status(200);
                return "Usuário alterado com sucesso";
            } catch (Exception e) {
                response.status(500);
                return "Erro";
            }
        });
    }
}

