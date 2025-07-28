/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 24/7/25
  Time: 22:32
*/
package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.services.DepositoService;
import edu.unl.cc.jbrew.services.RetiroSinTarjetaService;
import edu.unl.cc.jbrew.services.TransferenciaService;
import edu.unl.cc.jbrew.services.UsuarioService;
import edu.unl.cc.jbrew.services.SaldoService;
import edu.unl.cc.jbrew.domain.common.Deposito;
import edu.unl.cc.jbrew.domain.common.RetiroSinTarjeta;
import edu.unl.cc.jbrew.domain.common.Transferencia;
import edu.unl.cc.jbrew.domain.common.Usuario;
import edu.unl.cc.jbrew.domain.common.Saldo;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * Bean de vista para manejar la lógica principal de la página de inicio.
 * <p>
 * Gestiona el saldo del usuario actual y operaciones básicas como sumar depósitos,
 * restar retiros y reiniciar saldo.
 * </p>
 */
@Named("inicioBean")
@ViewScoped
public class InicioBean implements Serializable {

    @Inject
    private AuthenticationBean authenticationBean;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private DepositoService depositoService;

    @Inject
    private RetiroSinTarjetaService retiroSinTarjetaService;

    @Inject
    private TransferenciaService transferenciaService;

    @Inject
    private SaldoService saldoService;

    private static final Logger logger = Logger.getLogger(InicioBean.class.getName());

    private BigDecimal saldo = BigDecimal.ZERO;

    /**
     * Obtiene el saldo actual del usuario autenticado.
     * 
     * @return saldo actual o cero si el usuario no está autenticado o no tiene saldo
     */
    public BigDecimal getSaldo() {
        String ci = authenticationBean.getUsername();
        if (ci == null) return BigDecimal.ZERO;
        Saldo saldo = saldoService.obtenerPorCi(ci);
        return saldo != null ? saldo.getSaldo() : BigDecimal.ZERO;
    }

    /**
     * Suma un monto al saldo del usuario autenticado.
     * 
     * @param monto monto a sumar, debe ser mayor que cero
     */
    public void sumarDeposito(BigDecimal monto) {
        String ci = authenticationBean.getUsername();
        if (ci != null && monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            saldoService.sumarSaldo(ci, monto);
        }
    }

    /**
     * Resta un monto del saldo del usuario autenticado si es posible.
     * 
     * @param monto monto a restar, debe ser mayor que cero
     * @return {@code true} si se pudo restar el saldo, {@code false} si no hay saldo suficiente o datos inválidos
     */
    public boolean restarRetiro(BigDecimal monto) {
        String ci = authenticationBean.getUsername();
        if (ci != null && monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            return saldoService.restarSaldo(ci, monto);
        }
        return false;
    }

    /**
     * Reinicia el saldo del usuario autenticado a cero.
     */
    public void reiniciarSaldo() {
        String ci = authenticationBean.getUsername();
        if (ci != null) {
            saldoService.actualizarSaldo(ci, BigDecimal.ZERO);
        }
    }
}
