package edu.unl.cc.jbrew.controllers.security;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import jakarta.inject.Inject;
import edu.unl.cc.jbrew.domain.common.Saldo;
import edu.unl.cc.jbrew.services.SaldoService;
import edu.unl.cc.jbrew.controllers.AuthenticationBean;
import edu.unl.cc.jbrew.domain.common.RetiroTarjeta;
import edu.unl.cc.jbrew.services.RetiroTarjetaService;
import edu.unl.cc.jbrew.domain.common.Tarjeta;

@Named
@SessionScoped
public class RetiroFinalBean implements Serializable {

    private Double monto;
    private String codigoRetiro;

    @Inject
    private SaldoService saldoService;
    @Inject
    private AuthenticationBean authenticationBean;
    @Inject
    private RetiroTarjetaService retiroTarjetaService;

    public String procesarRetiro() {
        FacesContext context = FacesContext.getCurrentInstance();
        // Validar monto seleccionado
        if (monto == null || monto <= 0) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione un monto válido"));
            return null;
        }
        // Obtener el CI de la tarjeta desde sesión
        String ci = (String) context.getExternalContext().getSessionMap().get("ciRetiroTarjeta");
        if (ci == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo identificar el usuario de la tarjeta."));
            return null;
        }
        Saldo saldo = saldoService.obtenerPorCi(ci);
        java.math.BigDecimal montoBD = java.math.BigDecimal.valueOf(monto);
        if (saldo == null || saldo.getSaldo().compareTo(montoBD) < 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Saldo insuficiente para el retiro."));
            return null;
        }
        // Descontar el saldo en la tabla saldos
        boolean exito = saldoService.restarSaldo(ci, montoBD);
        System.out.println("DEBUG RETIRO FINAL: ¿Se restó el saldo? " + exito);
        // Generar número de transacción y guardar datos para el recibo
        String numeroTransaccion = java.util.UUID.randomUUID().toString();
        java.util.Date fecha = new java.util.Date();
        context.getExternalContext().getSessionMap().put("montoRetirado", monto);
        context.getExternalContext().getSessionMap().put("numeroTransaccion", numeroTransaccion);
        context.getExternalContext().getSessionMap().put("fechaRetiro", fecha);
        context.getExternalContext().getSessionMap().put("ciRetiro", ci);
        // Registrar el retiro en la base de datos
        Tarjeta tarjeta = (Tarjeta) context.getExternalContext().getSessionMap().get("tarjetaValidada");
        String numeroTarjeta = "";
        if (tarjeta != null && tarjeta.getNumero() != null) {
            numeroTarjeta = tarjeta.getNumero().replaceAll("\\s+", "");
        }
        RetiroTarjeta retiro = new RetiroTarjeta();
        retiro.setCi(ci);
        retiro.setNumeroTarjeta(numeroTarjeta);
        retiro.setMonto(montoBD);
        retiro.setFecha(fecha);
        retiro.setNumeroTransaccion(numeroTransaccion);
        retiroTarjetaService.registrarRetiroTarjeta(retiro);
        // Guardar también el número de tarjeta en sesión para el recibo
        context.getExternalContext().getSessionMap().put("numeroTarjeta", numeroTarjeta);
        System.out.println("DEBUG RECIBO: monto=" + monto + ", transaccion=" + numeroTransaccion + ", fecha=" + fecha + ", ci=" + ci + ", tarjeta=" + numeroTarjeta);

        return "/operaciones/recibo.xhtml?faces-redirect=true";
    }

    // Getters y Setters
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
    public String getCodigoRetiro() { return codigoRetiro; }
    public void setCodigoRetiro(String codigoRetiro) { this.codigoRetiro = codigoRetiro; }
}