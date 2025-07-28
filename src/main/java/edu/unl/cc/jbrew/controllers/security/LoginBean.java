/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 24/7/25
  Time: 22:40
*/


package edu.unl.cc.jbrew.controllers.security;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String ci;
    private String password;

    // Usuario y contraseña válidos (puedes cambiarlos)
    private final String VALID_USER = "1234567890";
    private final String VALID_PASS = "clave123";

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Solo validar credenciales (elimina las validaciones de campos vacíos)
        if (!ci.equals(VALID_USER)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ""));
            return null;
        }

        if (!password.equals(VALID_PASS)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ""));
            return null;
        }

        return "/inicio.xhtml?faces-redirect=true";
    }
    // Getters y Setters
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
