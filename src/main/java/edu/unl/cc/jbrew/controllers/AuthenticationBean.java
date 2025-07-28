package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.bussiness.SecurityFacade;
import edu.unl.cc.jbrew.controllers.security.UserPrincipal;
import edu.unl.cc.jbrew.controllers.security.UserSession;
import edu.unl.cc.jbrew.domain.security.User;
import edu.unl.cc.jbrew.faces.FacesUtil;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.logging.Logger;
import edu.unl.cc.jbrew.services.UsuarioService;
import edu.unl.cc.jbrew.domain.common.Usuario;

/**
 * Controlador JSF para manejar la autenticación de usuarios (login/logout).
 * Mantiene el estado durante la interacción con la vista (ViewScoped).
 * 
 * @see UsuarioService
 * @see SecurityFacade
 * @see UserSession
 */
@Named
@ViewScoped
public class AuthenticationBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AuthenticationBean.class.getName());

    // Credenciales del usuario con validaciones
    @NotNull(message = "El usuario no puede estar vacío")
    private String username;
    
    @NotNull(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // Dependencias inyectadas
    @Inject
    private SecurityFacade securityFacade;
    
    @Inject
    private UserSession userSession;
    
    @Inject
    private UsuarioService usuarioService;

    /**
     * Procesa el intento de autenticación del usuario.
     * 
     * @return String ruta de redirección después del login (inicio.xhtml si es exitoso, 
     *         null si falla para permanecer en la misma vista)
     */
    public String login() {
        logger.info("Intento de login para usuario: " + username);
        // NOTA: En producción, evitar loggear credenciales
        
        try {
            Usuario usuario = usuarioService.autenticar(username, password);
            
            if (usuario != null) {
                // Login exitoso
                FacesUtil.addMessageAndKeep(FacesMessage.SEVERITY_INFO, 
                    "Aviso", "Bienvenido " + usuario.getNombres() + " a la aplicación Jbrew.");
                
                // Establecer CI en sesión para uso posterior
                FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("ci", usuario.getCi());
                
                return "inicio.xhtml?faces-redirect=true";
            } else {
                // Credenciales inválidas
                FacesUtil.addMessage(FacesMessage.SEVERITY_ERROR, 
                    "Credenciales inválidas", "Verifica tu CI y contraseña.");
                return null;
            }
        } catch (Exception e) {
            // Manejo de errores genéricos
            String detalle = e.getMessage() != null && !e.getMessage().isEmpty() ? 
                e.getMessage() : "Verifica tu usuario y contraseña.";
                
            FacesUtil.addMessage(FacesMessage.SEVERITY_ERROR, detalle, detalle);
            return null;
        }
    }

    /**
     * Cierra la sesión actual del usuario e invalida la sesión HTTP.
     * 
     * @return String ruta de redirección al login
     * @throws ServletException si ocurre un error durante el logout del contenedor
     */
    public String logout() throws ServletException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        // 1. Invalidar la sesión actual
        facesContext.getExternalContext().invalidateSession();
        
        // 2. Realizar logout del contenedor
        ((jakarta.servlet.http.HttpServletRequest) facesContext.getExternalContext().getRequest()).logout();
        
        // 3. Mostrar mensaje de confirmación
        FacesUtil.addSuccessMessageAndKeep("Sesión cerrada exitosamente");
        
        return "/login.xhtml?faces-redirect=true";
    }

    /**
     * Verifica si existe una sesión de usuario activa.
     * 
     * @return boolean true si existe sesión activa, false en caso contrario
     */
    public boolean verifyUserSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().containsKey("user");
    }

    /**
     * Establece la información del usuario en la sesión HTTP.
     * 
     * @param user Objeto User con los datos del usuario autenticado
     */
    private void setHttpSession(User user) {
        FacesContext context = FacesContext.getCurrentInstance();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        context.getExternalContext().getSessionMap().put("user", userPrincipal);
    }

    // Getters y Setters con documentación adicional
    
    /**
     * Obtiene el nombre de usuario. Si está vacío, intenta recuperarlo de la sesión.
     * 
     * @return String nombre de usuario o CI
     */
    public String getUsername() {
        if (username == null || username.isEmpty()) {
            Object ci = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("ci");
                
            if (ci != null) {
                username = ci.toString();
            }
        }
        return username;
    }

    /**
     * Establece el nombre de usuario.
     * 
     * @param username String identificador del usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return String contraseña (sin encriptar)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param password String contraseña (sin encriptar)
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
