package edu.unl.cc.jbrew.controllers.security;

import edu.unl.cc.jbrew.domain.common.Usuario;
import edu.unl.cc.jbrew.services.UsuarioService;
import edu.unl.cc.jbrew.services.SaldoService;
import edu.unl.cc.jbrew.util.EncryptorManager;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;

@Named
@SessionScoped
public class RegistroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombres;
    private String genero;
    private Date fechaNacimiento;
    private String ci;
    private String password;
    private boolean terminosAceptados;
    private String email;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private SaldoService saldoService;

    @Transactional
    public String registrar() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Usuario existente = usuarioService.buscarPorCi(ci);
            if (existente != null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya existe un usuario con ese CI"));
                return null;
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombres(nombres);
            nuevoUsuario.setGenero(genero);
            nuevoUsuario.setFechaNacimiento(new java.sql.Date(fechaNacimiento.getTime()));
            nuevoUsuario.setCi(ci);
            nuevoUsuario.setPassword(EncryptorManager.encrypt(password));
            nuevoUsuario.setTerminosAceptados(terminosAceptados);
            nuevoUsuario.setEmail(email);

            usuarioService.registrarUsuario(nuevoUsuario);
            saldoService.crearSaldo(ci);
            limpiarFormulario();
            return "/registro-exitoso.xhtml?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo completar el registro: " + e.getMessage()));
            return null;
        }
    }

    private void limpiarFormulario() {
        this.nombres = null;
        this.genero = null;
        this.fechaNacimiento = null;
        this.ci = null;
        this.password = null;
        this.terminosAceptados = false;
    }

    // Getters y Setters
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isTerminosAceptados() { return terminosAceptados; }
    public void setTerminosAceptados(boolean terminosAceptados) { this.terminosAceptados = terminosAceptados; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
