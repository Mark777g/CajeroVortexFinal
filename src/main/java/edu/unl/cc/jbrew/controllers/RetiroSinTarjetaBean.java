package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.domain.common.RetiroSinTarjeta;
import edu.unl.cc.jbrew.services.RetiroSinTarjetaService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import edu.unl.cc.jbrew.controllers.AuthenticationBean;

/**
 * Managed Bean para manejar operaciones relacionadas con retiros sin tarjeta.
 * <p>
 * Permite registrar retiros, validando saldo suficiente y generando un código de retiro único si no se proporciona.
 * También permite listar los retiros realizados por el usuario autenticado.
 * </p>
 */
@Named
@RequestScoped
public class RetiroSinTarjetaBean implements Serializable {

    /**
     * Número de cédula del usuario que realiza el retiro.
     */
    private String ci;

    /**
     * Monto a retirar.
     */
    private BigDecimal monto;

    /**
     * Código único asociado al retiro.
     */
    private String codigoRetiro;

    /**
     * Servicio para operaciones de retiro sin tarjeta.
     */
    @Inject
    private RetiroSinTarjetaService retiroService;

    /**
     * Bean para operaciones de inicio, como actualización de saldo.
     */
    @Inject
    private InicioBean inicioBean;

    /**
     * Bean de autenticación para obtener el usuario actual.
     */
    @Inject
    private AuthenticationBean authenticationBean;

    /**
     * Registra un retiro sin tarjeta verificando que el saldo sea suficiente.
     * Si no se proporciona un código de retiro, se genera uno único automáticamente.
     * 
     * @return null para permanecer en la misma página, muestra mensajes de éxito o error.
     */
    public String registrarRetiro() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // Validar que haya saldo suficiente para el retiro
            if (!inicioBean.restarRetiro(monto)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Saldo insuficiente para el retiro."));
                return null;
            }
            RetiroSinTarjeta r = new RetiroSinTarjeta();
            r.setCi(ci);
            r.setMonto(monto);
            // Generar código único si no fue proporcionado
            if (codigoRetiro == null || codigoRetiro.trim().isEmpty()) {
                r.setCodigoRetiro(UUID.randomUUID().toString());
            } else {
                r.setCodigoRetiro(codigoRetiro);
            }
            retiroService.registrarRetiro(r);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Retiro registrado correctamente."));
            limpiarFormulario();
            return null;
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar el retiro: " + e.getMessage()));
            return null;
        }
    }

    /**
     * Obtiene la lista de retiros sin tarjeta realizados por el usuario autenticado.
     * 
     * @return lista de retiros; lista vacía si no hay usuario autenticado.
     */
    public List<RetiroSinTarjeta> getRetiros() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        return retiroService.listarRetiros().stream()
            .filter(r -> ci.equals(r.getCi()))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Limpia los campos del formulario después de registrar un retiro exitoso.
     */
    private void limpiarFormulario() {
        ci = null;
        monto = null;
        codigoRetiro = null;
    }

    // Getters y Setters
    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getCodigoRetiro() {
        return codigoRetiro;
    }

    public void setCodigoRetiro(String codigoRetiro) {
        this.codigoRetiro = codigoRetiro;
    }
}
