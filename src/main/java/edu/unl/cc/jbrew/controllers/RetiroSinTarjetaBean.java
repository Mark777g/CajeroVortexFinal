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

@Named
@RequestScoped
public class RetiroSinTarjetaBean implements Serializable {
    private String ci;
    private BigDecimal monto;
    private String codigoRetiro;

    @Inject
    private RetiroSinTarjetaService retiroService;

    @Inject
    private InicioBean inicioBean;

    @Inject
    private AuthenticationBean authenticationBean;

    public String registrarRetiro() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!inicioBean.restarRetiro(monto)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Saldo insuficiente para el retiro."));
                return null;
            }
            RetiroSinTarjeta r = new RetiroSinTarjeta();
            r.setCi(ci);
            r.setMonto(monto);
            // Si el usuario no proporciona un código, genera uno único
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

    public List<RetiroSinTarjeta> getRetiros() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        return retiroService.listarRetiros().stream()
            .filter(r -> ci.equals(r.getCi()))
            .collect(java.util.stream.Collectors.toList());
    }

    private void limpiarFormulario() {
        ci = null;
        monto = null;
        codigoRetiro = null;
    }

    // Getters y Setters
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getCodigoRetiro() { return codigoRetiro; }
    public void setCodigoRetiro(String codigoRetiro) { this.codigoRetiro = codigoRetiro; }
} 