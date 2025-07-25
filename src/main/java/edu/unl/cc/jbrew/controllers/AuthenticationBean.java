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

@Named
@ViewScoped
public class AuthenticationBean implements java.io.Serializable{

    private static Logger logger = Logger.getLogger(AuthenticationBean.class.getName());

    //@Inject
    //private Logger logger;

    @NotNull
    private String username;
    @NotNull
    @Size(min=6, message="Contraseña muy corta")
    private String password;

    @Inject
    private SecurityFacade securityFacade;

    @Inject
    private UserSession userSession;

    @Inject
    private UsuarioService usuarioService;

    //@Inject
    //private FacesContext facesContext;

    public String login(){
        logger.info("Logging in with username: " + username);
        logger.info("Logging in with password: " + password);
        try {
            Usuario usuario = usuarioService.autenticar(username, password);
            if (usuario != null) {
                FacesUtil.addMessageAndKeep(FacesMessage.SEVERITY_INFO, "Aviso", "Bienvenido " + usuario.getNombres() + " a la aplicación Jbrew.");
                // Guardar el CI en sesión
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ci", usuario.getCi());
                return "inicio.xhtml?faces-redirect=true";
            } else {
                FacesUtil.addMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", "Verifica tu CI y contraseña.");
                return null;
            }
        } catch (Exception e) {
            String detalle = e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : "Verifica tu usuario y contraseña.";
            FacesUtil.addMessage(FacesMessage.SEVERITY_ERROR, detalle, detalle);
            return null;
        }
    }

    public String logout() throws ServletException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().invalidateSession();
        FacesUtil.addSuccessMessageAndKeep("Logged out successfully");
        //FacesMessage fc = new FacesMessage("Logged out successfully");
        //facesContext.addMessage(null, fc);
        //facesContext.getExternalContext().getFlash().setKeepMessages(true);

        ((jakarta.servlet.http.HttpServletRequest) facesContext.getExternalContext().getRequest()).logout();
        //userSession.logout();
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean verifyUserSession(){
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().containsKey("user");
    }

    /**
     * Establece la sessión de usuario
     * @param user
     */
    private void setHttpSession(User user){
        FacesContext context = FacesContext.getCurrentInstance();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        context.getExternalContext().getSessionMap().put("user", userPrincipal);
    }

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
