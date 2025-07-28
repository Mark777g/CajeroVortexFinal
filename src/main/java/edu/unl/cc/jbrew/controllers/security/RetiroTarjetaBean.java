/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 25/7/25
  Time: 15:50
*/
package edu.unl.cc.jbrew.controllers.security;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.time.YearMonth;
import jakarta.inject.Inject;
import edu.unl.cc.jbrew.domain.common.Tarjeta;
import edu.unl.cc.jbrew.domain.common.Saldo;
import edu.unl.cc.jbrew.services.TarjetaService;
import edu.unl.cc.jbrew.services.SaldoService;
import edu.unl.cc.jbrew.controllers.AuthenticationBean;

@Named
@SessionScoped
public class RetiroTarjetaBean implements Serializable {

    private String numeroTarjeta;
    private String cvc;
    private String fechaExpiracion;
    private String nombreTitular;
    private String tipoTarjeta;

    @Inject
    private TarjetaService tarjetaService;
    @Inject
    private SaldoService saldoService;
    @Inject
    private AuthenticationBean authenticationBean;

    public String validarTarjeta() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Validación de número de tarjeta
        if (numeroTarjeta == null || numeroTarjeta.trim().isEmpty()) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El número de tarjeta es obligatorio"));
            return null;
        } else {
            String numeroLimpio = numeroTarjeta.replaceAll("\\s+", "");
            if (!numeroLimpio.matches("\\d{16}")) {
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El número de tarjeta debe tener 16 dígitos"));
                return null;
            }
            if (!validarAlgoritmoLuhn(numeroLimpio)) {
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El número de tarjeta no es válido"));
                return null;
            }
        }

        // Validación de CVC
        if (cvc == null || cvc.trim().isEmpty()) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El CVC es obligatorio"));
            return null;
        } else if (!cvc.matches("\\d{3}")) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El CVC debe tener 3 dígitos"));
            return null;
        }

        // Validación de fecha de expiración
        if (fechaExpiracion == null || fechaExpiracion.trim().isEmpty()) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La fecha de expiración es obligatoria"));
            return null;
        } else {
            // Validar formato yyyy-MM-dd
            if (!fechaExpiracion.matches("\\d{4}-\\d{2}-\\d{2}")) {
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El formato debe ser yyyy-MM-dd"));
                return null;
            }
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Date fechaExp = sdf.parse(fechaExpiracion);
                java.util.Date hoy = new java.util.Date();
                if (fechaExp.before(hoy)) {
                    context.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La tarjeta está expirada"));
                    return null;
                }
            } catch (Exception e) {
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Fecha de expiración inválida"));
                return null;
            }
        }

        // Validación de nombre del titular
        if (nombreTitular == null || nombreTitular.trim().isEmpty()) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre del titular es obligatorio"));
            return null;
        } else if (!nombreTitular.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre solo debe contener letras y espacios"));
            return null;
        }

        // Validación de tipo de tarjeta
        if (tipoTarjeta == null || tipoTarjeta.trim().isEmpty() || "Seleccione tipo".equals(tipoTarjeta)) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un tipo de tarjeta"));
            return null;
        }

        // Validación en base de datos:
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaExp = sdf.parse(fechaExpiracion);
            Tarjeta tarjeta = tarjetaService.buscarTarjetaPorDatos(numeroTarjeta, cvc, fechaExp);
            if (tarjeta == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Datos de tarjeta incorrectos o tarjeta inactiva"));
                return null;
            }
            // Validar saldo suficiente usando el CI de la tarjeta
            String ciTarjeta = tarjeta.getCi();
            System.out.println("DEBUG RETIRO TARJETA: CI obtenido de la tarjeta = " + ciTarjeta);
            Saldo saldo = saldoService.obtenerPorCi(ciTarjeta);
            System.out.println("DEBUG RETIRO TARJETA: Saldo obtenido para CI " + ciTarjeta + " = " + (saldo != null ? saldo.getSaldo() : "null"));
            if (saldo == null || saldo.getSaldo().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No tiene saldo suficiente para retirar. Saldo actual: " + (saldo != null ? saldo.getSaldo() : "0")));
                return null;
            }
            // Guardar datos en sesión para el siguiente paso
            context.getExternalContext().getSessionMap().put("tarjetaValidada", tarjeta);
            context.getExternalContext().getSessionMap().put("ciRetiroTarjeta", ciTarjeta);
            return "/retiroFinal.xhtml?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error validando tarjeta: " + e.getMessage()));
            return null;
        }
    }

    private boolean validarAlgoritmoLuhn(String numero) {
        int sum = 0;
        boolean alternate = false;

        for (int i = numero.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(numero.substring(i, i + 1));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }

    // Getters y Setters
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public String getCvc() { return cvc; }
    public void setCvc(String cvc) { this.cvc = cvc; }

    public String getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }

    public String getTipoTarjeta() { return tipoTarjeta; }
    public void setTipoTarjeta(String tipoTarjeta) { this.tipoTarjeta = tipoTarjeta; }
}
