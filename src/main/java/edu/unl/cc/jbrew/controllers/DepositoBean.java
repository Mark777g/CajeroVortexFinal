package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.domain.common.Deposito;
import edu.unl.cc.jbrew.services.DepositoService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import edu.unl.cc.jbrew.controllers.AuthenticationBean;

/**
 * Managed Bean para manejar operaciones relacionadas con depósitos.
 * <p>
 * Permite registrar depósitos asociados a un usuario y listar los depósitos realizados
 * por el usuario actualmente autenticado.
 * </p>
 */
@Named
@RequestScoped
public class DepositoBean implements Serializable {

    /**
     * Número de cédula del usuario que realiza el depósito.
     */
    private String ci;

    /**
     * Monto del depósito.
     */
    private BigDecimal monto;

    /**
     * Descripción opcional del depósito.
     */
    private String descripcion;

    /**
     * Servicio para operaciones con depósitos.
     */
    @Inject
    private DepositoService depositoService;

    /**
     * Bean para operaciones de inicio, como actualizar saldos.
     */
    @Inject
    private InicioBean inicioBean;

    /**
     * Bean de autenticación para obtener el usuario actual.
     */
    @Inject
    private AuthenticationBean authenticationBean;

    /**
     * Registra un nuevo depósito utilizando los datos proporcionados en el formulario.
     * Actualiza el estado del depósito a "ACTIVO" y suma el monto al saldo general.
     * 
     * @return null para permanecer en la misma página, muestra mensajes informativos o de error.
     */
    public String registrarDeposito() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Deposito d = new Deposito();
            d.setCi(ci);
            d.setMonto(monto);
            d.setDescripcion(descripcion);
            depositoService.registrarDeposito(d);
            // Actualizar el estado a ACTIVO
            depositoService.actualizarEstadoADepositoActivo(d.getId());
            inicioBean.sumarDeposito(monto);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Depósito registrado correctamente."));
            limpiarFormulario();
            return null;
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar el depósito: " + e.getMessage()));
            return null;
        }
    }

    /**
     * Obtiene la lista de depósitos asociados al usuario actualmente autenticado.
     * 
     * @return lista de depósitos del usuario; lista vacía si no hay usuario autenticado.
     */
    public List<Deposito> getDepositos() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        return depositoService.listarDepositos().stream()
            .filter(d -> ci.equals(d.getCi()))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Limpia los campos del formulario después de un registro exitoso.
     */
    private void limpiarFormulario() {
        ci = null;
        monto = null;
        descripcion = null;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
