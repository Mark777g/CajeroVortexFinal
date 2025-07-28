package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.domain.common.Tarjeta;
import edu.unl.cc.jbrew.services.TarjetaService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import edu.unl.cc.jbrew.controllers.AuthenticationBean;

@Named
@RequestScoped
public class TarjetaBean implements Serializable {
    private String ci;
    private String numero;
    private String tipo;
    private Date fechaExpiracion;
    private String nombreTitular;
    private String cvc;

    @Inject
    private TarjetaService tarjetaService;

    @Inject
    private AuthenticationBean authenticationBean;

    public String registrarTarjeta() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Tarjeta t = new Tarjeta();
            t.setCi(ci);
            t.setNumero(numero);
            t.setTipo(tipo);
            t.setFechaExpiracion(fechaExpiracion);
            t.setNombreTitular(nombreTitular);
            t.setCvc(cvc);
            tarjetaService.registrarTarjeta(t);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Tarjeta registrada correctamente."));
            limpiarFormulario();
            return null;
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar la tarjeta: " + e.getMessage()));
            return null;
        }
    }

    public List<Tarjeta> getTarjetas() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        return tarjetaService.listarTarjetas().stream()
            .filter(t -> ci.equals(t.getCi()))
            .collect(java.util.stream.Collectors.toList());
    }

    private void limpiarFormulario() {
        ci = null;
        numero = null;
        tipo = null;
        fechaExpiracion = null;
        nombreTitular = null;
        cvc = null;
    }

    // Getters y Setters
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Date getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Date fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }
    public String getCvc() { return cvc; }
    public void setCvc(String cvc) { this.cvc = cvc; }
} 
