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
 * Managed Bean encargado de la autenticación y gestión de la sesión de usuario
 * dentro de la aplicación JSF.
 * <p>
 * Controla el inicio y cierre de sesión, así como la validación de credenciales
 * a través del servicio {@link UsuarioService}.
 * </p>
 */
@Named
@ViewScoped
public class AuthenticationBean implements java.io.Serializable {

    private static final Logger logger = Logger.getLogger(AuthenticationBean.class.getName());

    /**
     * Nombre de usuario (CI). Es obligatorio para el login.
     */
    @NotNull
    private String username;

    /**
     * Contraseña del usuario. Debe tener al menos 6 caracteres.
     */
    @NotNull
    @Size(min = 6, message = "Contraseña muy corta")
    private String password;

    /**
     * Fachada para seguridad (no usada en la lógica actual pero disponible para extender).
     */
    @Inject
    private SecurityFacade securityFacade;

    /**
     * Sesión del usuario actual.
     */
    @Inject
    private UserSession userSession;

    /**
     * Servicio para operaciones relacionadas con usuarios (autenticación y búsqueda).
     */
    @Inject
    private UsuarioService usuarioService;

    /**
     * Intenta autenticar al usuario con las credenciales proporcionadas.
     * Si son correctas, almacena el CI en la sesión y redirige a la página de inicio.
     *
     * @return la navegación a la página de inicio si es exitoso, {@code null} en caso de error.
     */
    public String login() {
        logger.info("Logging in with username: " + username);
        logger.info("Logging in with password: " + password);
        try {
            Usuario usuario = usuarioService.autenticar(username, password);
            if (usuario != null) {
                FacesUtil.addMessageAndKeep(FacesMessage.SEVERITY_INFO, "Aviso",
                        "Bienvenido " + usuario.getNombres() + " a la aplicación Jbrew.");
                // Guardar el CI en sesión
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ci", usuario.getCi());
                return "inicio.xhtml?faces-redirect=true";
            } else {
                FacesUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", "Verifica tu CI y contraseña.");
                return null;
            }
        } catch (Exception e) {
            String detalle = e.getMessage() != null && !e.getMessage().isEmpty()
                    ? e.getMessage()
                    : "Verifica tu usuario y contraseña.";
            FacesUtil.addMessage(FacesMessage.SEVERITY_ERROR, detalle, detalle);
            return null;
        }
    }

    /**
     * Cierra la sesión del usuario actual e invalida la sesión HTTP.
     *
     * @return la navegación a la página de login después del cierre de sesión.
     * @throws ServletException si ocurre un error al cerrar la sesión en el contenedor.
     */
    public String logout() throws ServletException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().invalidateSession();
        FacesUtil.addSuccessMessageAndKeep("Logged out successfully");

        ((jakarta.servlet.http.HttpServletRequest) facesContext.getExternalContext().getRequest()).logout();
        return "/login.xhtml?faces-redirect=true";
    }

    /**
     * Verifica si existe una sesión de usuario activa.
     *
     * @return {@code true} si hay un usuario en sesión; {@code false} en caso contrario.
     */
    public boolean verifyUserSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().containsKey("user");
    }

    /**
     * Establece el usuario en la sesión HTTP a través de {@link UserPrincipal}.
     *
     * @param user objeto de tipo {@link User} que representa al usuario autenticado.
     */
    private void setHttpSession(User user) {
        FacesContext context = FacesContext.getCurrentInstance();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        context.getExternalContext().getSessionMap().put("user", userPrincipal);
    }

    /**
     * Obtiene el nombre de usuario actual.
     * Si no se ha establecido, intenta recuperar el CI desde la sesión.
     *
     * @return el nombre de usuario (CI).
     */
    public String getUsername() {
        if (username == null || username.isEmpty()) {
            Object ci = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ci");
            if (ci != null) {
                username = ci.toString();
            }
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
