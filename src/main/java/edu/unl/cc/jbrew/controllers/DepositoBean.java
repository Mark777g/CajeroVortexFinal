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

@Named
@RequestScoped
public class DepositoBean implements Serializable {
    private String ci;
    private BigDecimal monto;
    private String descripcion;

    @Inject
    private DepositoService depositoService;

    @Inject
    private InicioBean inicioBean;

    @Inject
    private AuthenticationBean authenticationBean;

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

    public List<Deposito> getDepositos() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        return depositoService.listarDepositos().stream()
            .filter(d -> ci.equals(d.getCi()))
            .collect(java.util.stream.Collectors.toList());
    }

    private void limpiarFormulario() {
        ci = null;
        monto = null;
        descripcion = null;
    }

    // Getters y Setters
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
} 