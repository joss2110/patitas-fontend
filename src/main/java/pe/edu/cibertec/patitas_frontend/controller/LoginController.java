package pe.edu.cibertec.patitas_frontend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.cibertec.patitas_frontend.viewmodel.LoginModel;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping("/inicio")
    public String inicio(Model model) {

        LoginModel loginModel = new LoginModel("00", "", "","");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             @RequestParam("password") String password,
                             Model model) {

        if (isNullOrEmpty(tipoDocumento) || isNullOrEmpty(numeroDocumento) || isNullOrEmpty(password)) {
            LoginModel loginModel = new LoginModel("02", "Error: credenciales incompletas", "","");
            model.addAttribute("loginModel", loginModel);
            return "inicio";
        }

        Map<String, String> loginRequestMap = Map.of(
                "tipoDocumento", tipoDocumento,
                "numeroDocumento", numeroDocumento,
                "password", password
        );

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Serialización a JSON
            String requestBody = objectMapper.writeValueAsString(loginRequestMap);

            // Enviar la solicitud HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/autenticacion/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Procesar la respuesta
            Map<String, String> loginResponseDTO = objectMapper.readValue(response.body(), new TypeReference<Map<String, String>>() {});
            String codigo = loginResponseDTO.get("codigo");
            String mensaje = loginResponseDTO.get("mensaje");

            // Crear el modelo y añadirlo al atributo
            LoginModel loginModel = new LoginModel(codigo, mensaje, loginResponseDTO.get("nombreUsuario"), loginResponseDTO.get("correoUsuario"));
            model.addAttribute("loginModel", loginModel);

            // Lógica de redirección
            if (!"00".equals(codigo)) {
                return "inicio";
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            model.addAttribute("loginModel", new LoginModel("03", "Error: Problema con el servicio de autenticación", "",""));
            return "inicio";
        }

        return "principal";
    }
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
