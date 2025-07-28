/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 24/7/25
  Time: 23:55
*/

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
 * Filtro de autorización para controlar el acceso a las páginas de la aplicación.
 * <p>
 * Permite el acceso libre a recursos públicos y páginas de login e índice.
 * Para el resto, verifica que exista un usuario autenticado y que tenga permisos para la página solicitada.
 * </p>
 * <p>
 * Actualmente está deshabilitado (comentada la anotación {@code @WebFilter}).
 * Para activarlo, descomentar dicha anotación y configurar los patrones de URL necesarios.
 * </p>
 */
//@WebFilter("*.xhtml")
public class AuthorizationFilter implements Filter {

    /**
     * Sesión del usuario inyectada para obtener información del usuario autenticado
     * y verificar permisos.
     */
    @Inject
    UserSession userSession;

    /**
     * Filtra las peticiones HTTP verificando la autenticación y autorización.
     * 
     * @param servletRequest  petición entrante
     * @param servletResponse respuesta a enviar
     * @param filterChain     cadena de filtros para continuar el proceso
     * @throws IOException      si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error en el servlet
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        String requestPath = httpReq.getServletPath();

        // 1. Permitir recursos públicos sin restricción
        if (requestPath.startsWith("/public/") || requestPath.equals("/login.xhtml") || requestPath.equals("/index.xhtml")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 2. Obtener usuario autenticado desde la sesión
        User user = userSession.getUser();

        // 3. Redirigir a login si no está autenticado
        if (user == null) {
            ((HttpServletResponse) servletResponse).sendRedirect(httpReq.getContextPath() + "/login.xhtml");
            return;
        }

        // 4. Verificar permisos para la página solicitada
        if (userSession.hasPermissionForPage(requestPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
        }
    }
}
