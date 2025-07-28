/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 24/7/25
  Time: 22:40
*/


package edu.unl.cc.jbrew.controllers.security;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class TarjetasBean implements Serializable {

    private String tarjetaSeleccionada;
    private List<String> tarjetasActivas = new ArrayList<>();
    private boolean mostrarDetalles = false;
    private String numeroTarjeta;
    private String cvv;
    private String tipoTarjeta;
    private String nombreTitular;
    private String fechaExpiracion;

    @PostConstruct
    public void init() {
        // Simulación de tarjetas (en producción vendría de la base de datos)
        if (tarjetasActivas.isEmpty()) {
            tarjetasActivas.add("VISA •••• 4512");
            tarjetasActivas.add("MASTERCARD •••• 7845");
        }
    }

    public void onTarjetaChange() {
        mostrarDetalles = (tarjetaSeleccionada != null && !tarjetaSeleccionada.isEmpty());

        if (mostrarDetalles) {
            // Simular datos de tarjeta seleccionada
            if (tarjetaSeleccionada.contains("VISA")) {
                numeroTarjeta = "4512-7896-3254-7845";
                cvv = "123";
                tipoTarjeta = "VISA Débito";
                nombreTitular = "Juan Pérez";
                fechaExpiracion = "12/25";
            } else {
                numeroTarjeta = "7845-9632-1452-3658";
                cvv = "456";
                tipoTarjeta = "MASTERCARD Crédito";
                nombreTitular = "Juan Pérez";
                fechaExpiracion = "10/24";
            }
        }
    }

    public String solicitarTarjetas() {
        return "solicitarTarjetas?faces-redirect=true";
    }

    // Getters y Setters
    public String getTarjetaSeleccionada() { return tarjetaSeleccionada; }
    public void setTarjetaSeleccionada(String tarjetaSeleccionada) {
        this.tarjetaSeleccionada = tarjetaSeleccionada;
        this.onTarjetaChange();
    }

    public List<String> getTarjetasActivas() { return tarjetasActivas; }
    public boolean isMostrarDetalles() { return mostrarDetalles; }
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public String getCvv() { return cvv; }
    public String getTipoTarjeta() { return tipoTarjeta; }
    public String getNombreTitular() { return nombreTitular; }
    public String getFechaExpiracion() { return fechaExpiracion; }
}
