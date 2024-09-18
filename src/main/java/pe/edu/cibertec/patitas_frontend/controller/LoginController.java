package pe.edu.cibertec.patitas_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.cibertec.patitas_frontend.viewmodel.LoginModel;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping("/inicio")
    public String inicio(Model model) {

        LoginModel loginModel = new LoginModel("00", "", "");
        model.addAttribute("loginModel", loginModel);
        return "inicio";


    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             @RequestParam("password") String password,
                             Model model) {

        if (tipoDocumento == null || tipoDocumento.trim().length() == 0 ||
                numeroDocumento == null || numeroDocumento.trim().length() == 0 ||
                password == null || password.trim().length() == 0) {
            LoginModel loginModel = new LoginModel("01", "Error, debe completar correctamente sus credenciales", "Cesar Santos");
            model.addAttribute("loginModel", loginModel);
            return "inicio";
        }

        LoginModel loginModel = new LoginModel("00", "", "Cesar Santos");
        model.addAttribute("loginModel", loginModel);
        return "principal";
    }
}
