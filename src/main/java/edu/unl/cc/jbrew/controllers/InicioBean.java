package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.services.*;
import edu.unl.cc.jbrew.domain.common.*;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * Managed Bean para gestionar las operaciones financieras principales.
 * Controla el saldo y las transacciones del usuario autenticado.
 * 
 * <p>Alcance: ViewScoped (mantiene estado durante la interacción con la vista)</p>
 */
@Named("inicioBean")
@ViewScoped
public class InicioBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(InicioBean.class.getName());

    // Servicios inyectados
    @Inject private AuthenticationBean authenticationBean;
    @Inject private UsuarioService usuarioService;
    @Inject private DepositoService depositoService;
    @Inject private RetiroSinTarjetaService retiroSinTarjetaService;
    @Inject private TransferenciaService transferenciaService;
    @Inject private SaldoService saldoService;

    /**
     * Obtiene el saldo actual del usuario autenticado.
     * 
     * @return BigDecimal saldo actual o BigDecimal.ZERO si no hay usuario autenticado
     */
    public BigDecimal getSaldo() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            logger.warning("Intento de obtener saldo sin usuario autenticado");
            return BigDecimal.ZERO;
        }
        
        Saldo saldo = saldoService.obtenerPorCi(ci);
        return saldo != null ? saldo.getSaldo() : BigDecimal.ZERO;
    }

    /**
     * Incrementa el saldo del usuario con un depósito.
     * 
     * @param monto cantidad a depositar (debe ser mayor que cero)
     */
    public void sumarDeposito(BigDecimal monto) {
        String ci = authenticationBean.getUsername();
        if (ci != null && monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            saldoService.sumarSaldo(ci, monto);
            logger.info(String.format("Depósito de %s realizado para CI: %s", monto, ci));
        } else {
            logger.warning("Intento de depósito inválido - CI: " + ci + ", Monto: " + monto);
        }
    }

    /**
     * Reduce el saldo del usuario por un retiro.
     * 
     * @param monto cantidad a retirar (debe ser mayor que cero)
     * @return boolean true si el retiro fue exitoso, false si falló
     */
    public boolean restarRetiro(BigDecimal monto) {
        String ci = authenticationBean.getUsername();
        if (ci != null && monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            boolean resultado = saldoService.restarSaldo(ci, monto);
            logger.info(String.format("Retiro de %s %s para CI: %s", 
                monto, resultado ? "exitoso" : "fallido", ci));
            return resultado;
        }
        logger.warning("Intento de retiro inválido - CI: " + ci + ", Monto: " + monto);
        return false;
    }

    /**
     * Reinicia el saldo del usuario a cero.
     */
    public void reiniciarSaldo() {
        String ci = authenticationBean.getUsername();
        if (ci != null) {
            saldoService.actualizarSaldo(ci, BigDecimal.ZERO);
            logger.info("Saldo reiniciado para CI: " + ci);
        } else {
            logger.warning("Intento de reiniciar saldo sin usuario autenticado");
        }
    }

    // ==================== MÉTODOS ADICIONALES RECOMENDADOS ==================== //
    
    /**
     * Obtiene el historial de depósitos del usuario.
     * 
     * @return List<Deposito> lista de depósitos ordenados por fecha (descendente)
     */
    public List<Deposito> getHistorialDepositos() {
        String ci = authenticationBean.getUsername();
        if (ci == null) return List.of();
        
        return depositoService.listarPorUsuario(ci).stream()
            .sorted((d1, d2) -> d2.getFecha().compareTo(d1.getFecha()))
            .collect(Collectors.toList());
    }

    /**
     * Obtiene el historial de retiros sin tarjeta del usuario.
     * 
     * @return List<RetiroSinTarjeta> lista de retiros ordenados por fecha (descendente)
     */
    public List<RetiroSinTarjeta> getHistorialRetiros() {
        String ci = authenticationBean.getUsername();
        if (ci == null) return List.of();
        
        return retiroSinTarjetaService.listarPorUsuario(ci).stream()
            .sorted((r1, r2) -> r2.getFecha().compareTo(r1.getFecha()))
            .collect(Collectors.toList());
    }

    /**
     * Obtiene el historial de transferencias del usuario.
     * 
     * @return List<Transferencia> lista de transferencias ordenadas por fecha (descendente)
     */
    public List<Transferencia> getHistorialTransferencias() {
        String ci = authenticationBean.getUsername();
        if (ci == null) return List.of();
        
        return transferenciaService.listarPorUsuario(ci).stream()
            .sorted((t1, t2) -> t2.getFecha().compareTo(t1.getFecha()))
            .collect(Collectors.toList());
    }
}
