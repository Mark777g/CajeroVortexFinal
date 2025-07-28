/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 25/7/25
  Time: 15:50
*/

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


/**
 * Bean de sesión encargado de manejar el registro de nuevos usuarios en el sistema.
 * Mantiene los datos del formulario mientras dure la sesión.
 */
@Named
@SessionScoped
public class RegistroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Nombres completos del usuario a registrar */
    private String nombres;

    /** Género del usuario (ej. masculino, femenino, etc.) */
    private String genero;

    /** Fecha de nacimiento del usuario */
    private Date fechaNacimiento;

    /** Número de cédula de identidad del usuario */
    private String ci;

    /** Contraseña del usuario (será encriptada antes de persistir) */
    private String password;

    /** Indica si el usuario aceptó los términos y condiciones */
    private boolean terminosAceptados;

    /** Correo electrónico del usuario */
    private String email;

    /** Servicio para operaciones relacionadas con usuarios */
    @Inject
    private UsuarioService usuarioService;

    /** Servicio para manejo de saldos */
    @Inject
    private SaldoService saldoService;

    /**
     * Método principal de registro.
     * Valida si el usuario ya existe, encripta la contraseña,
     * guarda el nuevo usuario y crea su saldo inicial.
     * Si todo es exitoso, redirige a una página de confirmación.
     *
     * @return la ruta a la vista de éxito o null si ocurre un error
     */
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

    /**
     * Limpia los campos del formulario de registro.
     * Se llama después de un registro exitoso.
     */
    private void limpiarFormulario() {
        this.nombres = null;
        this.genero = null;
        this.fechaNacimiento = null;
        this.ci = null;
        this.password = null;
        this.terminosAceptados = false;
    }

    // Getters y Setters

    /** @return los nombres ingresados */
    public String getNombres() { return nombres; }

    /** @param nombres los nombres a establecer */
    public void setNombres(String nombres) { this.nombres = nombres; }

    /** @return el género ingresado */
    public String getGenero() { return genero; }

    /** @param genero el género a establecer */
    public void setGenero(String genero) { this.genero = genero; }

    /** @return la fecha de nacimiento */
    public Date getFechaNacimiento() { return fechaNacimiento; }

    /** @param fechaNacimiento la fecha de nacimiento a establecer */
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    /** @return el número de CI */
    public String getCi() { return ci; }

    /** @param ci la cédula de identidad a establecer */
    public void setCi(String ci) { this.ci = ci; }

    /** @return la contraseña del usuario */
    public String getPassword() { return password; }

    /** @param password la contraseña a establecer */
    public void setPassword(String password) { this.password = password; }

    /** @return true si los términos fueron aceptados */
    public boolean isTerminosAceptados() { return terminosAceptados; }

    /** @param terminosAceptados valor que indica si se aceptaron los términos */
    public void setTerminosAceptados(boolean terminosAceptados) { this.terminosAceptados = terminosAceptados; }

    /** @return el email ingresado */
    public String getEmail() { return email; }

    /** @param email el email a establecer */
    public void setEmail(String email) { this.email = email; }
}
