package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.controllers.security.UserPrincipal;
import edu.unl.cc.jbrew.controllers.security.UserSession;
import edu.unl.cc.jbrew.domain.security.User;
import edu.unl.cc.jbrew.faces.FacesUtil;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro de autorización que controla el acceso a las páginas de la aplicación.
 * Verifica la autenticación y los permisos del usuario antes de permitir el acceso.
 * 
 * <p>Actualmente anotado como comentario {@code @WebFilter} para deshabilitar su registro automático.
 * Para activarlo, descomentar la anotación y configurar los patrones de URL adecuados.</p>
 */
//@WebFilter("*.xhtml")
public class AuthorizationFilter implements Filter {

    @Inject
    private UserSession userSession;

    /**
     * Filtra las peticiones HTTP para controlar el acceso a los recursos protegidos.
     * 
     * @param servletRequest la petición recibida
     * @param servletResponse la respuesta a enviar
     * @param filterChain cadena de filtros a ejecutar
     * @throws IOException si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error en el servlet
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, 
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        String requestPath = httpReq.getServletPath();

        // 1. Permitir acceso a recursos públicos sin autenticación
        if (isPublicResource(requestPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 2. Obtener usuario autenticado desde la sesión
        User user = userSession.getUser();

        // 3. Redirigir al login si no está autenticado
        if (user == null) {
            redirectToLogin(httpReq, (HttpServletResponse) servletResponse);
            return;
        }

        // 4. Verificar permisos para la ruta solicitada
        handleAuthorizedAccess(userSession, requestPath, servletRequest, 
                servletResponse, filterChain);
    }

    /**
     * Determina si la ruta solicitada es un recurso público.
     * 
     * @param path la ruta solicitada
     * @return true si es un recurso público, false en caso contrario
     */
    private boolean isPublicResource(String path) {
        return path.startsWith("/public/") 
                || path.equals("/login.xhtml") 
                || path.equals("/index.xhtml");
    }

    /**
     * Redirige al usuario a la página de login.
     * 
     * @param request la petición HTTP
     * @param response la respuesta HTTP
     * @throws IOException si ocurre un error al redirigir
     */
    private void redirectToLogin(HttpServletRequest request, 
            HttpServletResponse response) throws IOException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/login.xhtml");
    }

    /**
     * Maneja el acceso autorizado verificando los permisos del usuario.
     * 
     * @param session la sesión del usuario
     * @param path la ruta solicitada
     * @param request la petición
     * @param response la respuesta
     * @param chain la cadena de filtros
     * @throws IOException si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error en el servlet
     */
    private void handleAuthorizedAccess(UserSession session, String path,
            ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        if (session.hasPermissionForPage(path)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendError(
                    HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
        }
    }
}
