package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Usuario;
import org.example.service.UsuarioService;
import org.example.dto.UsuarioDTOInput;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceTest {

    private static final String API_URL = "http://localhost:8080/api/usuarios";
    private static final String RANDOM_USER_API_URL = "https://randomuser.me/api/";

    @Test
    public void testInserirUsuario() {
        UsuarioService usuarioService = new UsuarioService();
        ServiceTest serviceTest = new ServiceTest();

        UsuarioDTOInput usuarioDTOInput = serviceTest.createUsuarioDTOInput();
        usuarioService.inserir(usuarioDTOInput);
        assertEquals(1, usuarioService.listar().size());
    }

    @Test
    public void testListarUsuarios() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("GET");
            int statusCode = connection.getResponseCode();
            assertEquals(200, statusCode);
        } finally {
            connection.disconnect();
        }
    }
    @Test
    public void testInserirRandomUser() throws IOException {
        JsonNode randomUserApiResponse = fetchApiData(RANDOM_USER_API_URL);
        JsonNode userData = randomUserApiResponse.path("results").get(0);

        Usuario usuario = new Usuario();
        usuario.setId(userData.path("id").asInt());
        usuario.setSenha(userData.path("senha").asText());

        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInputString = objectMapper.writeValueAsString(usuario);
            connection.getOutputStream().write(jsonInputString.getBytes());
            assertEquals(201, connection.getResponseCode());
        } finally {
            connection.disconnect();
        }
    }

    private JsonNode fetchApiData(String apiUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        try (InputStream inputStream = connection.getInputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(inputStream);
        } finally {
            connection.disconnect();
        }
    }

    private UsuarioDTOInput createUsuarioDTOInput() {
        UsuarioDTOInput usuarioDTOInput = new UsuarioDTOInput();
        usuarioDTOInput.setId(1);
        usuarioDTOInput.setSenha("password1243");
        return usuarioDTOInput;
    }
}
