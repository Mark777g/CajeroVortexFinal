package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.domain.common.Transferencia;
import edu.unl.cc.jbrew.services.TransferenciaService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import edu.unl.cc.jbrew.services.SaldoService;
import edu.unl.cc.jbrew.controllers.AuthenticationBean;

@Named
@RequestScoped
public class TransferenciaBean implements Serializable {
    private String cuentaOrigen;
    private String cuentaDestino;
    private BigDecimal monto;
    private String descripcion;

    @Inject
    private TransferenciaService transferenciaService;
    @Inject
    private InicioBean inicioBean;
    @Inject
    private SaldoService saldoService;
    @Inject
    private AuthenticationBean authenticationBean;

    public String registrarTransferencia() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!inicioBean.restarRetiro(monto)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Saldo insuficiente para la transferencia."));
                return null;
            }
            Transferencia t = new Transferencia();
            t.setCuentaOrigen(cuentaOrigen);
            t.setCuentaDestino(cuentaDestino);
            t.setMonto(monto);
            t.setDescripcion(descripcion);
            t.setCi(authenticationBean.getUsername());
            transferenciaService.registrarTransferencia(t);
            saldoService.sumarSaldo(cuentaDestino, monto);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Transferencia registrada correctamente."));
            limpiarFormulario();
            return null;
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar la transferencia: " + e.getMessage()));
            return null;
        }
    }

    public List<Transferencia> getTransferencias() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        return transferenciaService.listarTransferencias().stream()
            .filter(t -> ci.equals(t.getCuentaOrigen()) || ci.equals(t.getCuentaDestino()))
            .collect(java.util.stream.Collectors.toList());
    }

    private void limpiarFormulario() {
        cuentaOrigen = null;
        cuentaDestino = null;
        monto = null;
        descripcion = null;
    }

    // Getters y Setters
    public String getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }
    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
} 