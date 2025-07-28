/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 24/7/25
  Time: 19:57
*/

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

/**
 * Managed Bean para gestionar operaciones de transferencia entre cuentas.
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Registro de nuevas transferencias</li>
 *   <li>Validación de saldo disponible</li>
 *   <li>Actualización de saldos en cuentas involucradas</li>
 *   <li>Consulta de historial de transferencias</li>
 * </ul>
 * 
 * <p><strong>Alcance:</strong> RequestScoped (nueva instancia por cada petición HTTP)</p>
 */
@Named
@RequestScoped
public class TransferenciaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // Campos del formulario
    private String cuentaOrigen;      // Cuenta origen de la transferencia
    private String cuentaDestino;     // Cuenta destino de la transferencia
    private BigDecimal monto;         // Monto a transferir
    private String descripcion;       // Descripción/motivo de la transferencia

    // Servicios inyectados
    @Inject
    private TransferenciaService transferenciaService;  // Servicio para operaciones de transferencia
    
    @Inject
    private InicioBean inicioBean;                     // Bean para verificar saldo
    
    @Inject
    private SaldoService saldoService;                 // Servicio para actualizar saldos
    
    @Inject
    private AuthenticationBean authenticationBean;     // Bean de autenticación

    /**
     * Registra una nueva transferencia entre cuentas.
     * 
     * @return String navegación (siempre null para permanecer en la misma vista)
     * @throws IllegalStateException si ocurre un error durante el registro
     */
    public String registrarTransferencia() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // 1. Validar datos básicos
            if (!validarDatosTransferencia()) {
                context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Datos de transferencia incompletos o inválidos"));
                return null;
            }

            // 2. Verificar saldo suficiente
            if (!inicioBean.restarRetiro(monto)) {
                context.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "Saldo insuficiente para la transferencia."));
                return null;
            }

            // 3. Crear entidad Transferencia
            Transferencia t = new Transferencia();
            t.setCuentaOrigen(cuentaOrigen);
            t.setCuentaDestino(cuentaDestino);
            t.setMonto(monto);
            t.setDescripcion(descripcion);
            t.setCi(authenticationBean.getUsername());

            // 4. Persistir la transferencia
            transferenciaService.registrarTransferencia(t);
            
            // 5. Actualizar saldos
            saldoService.sumarSaldo(cuentaDestino, monto);
            
            // 6. Mostrar mensaje de éxito
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Éxito", "Transferencia registrada correctamente."));
            
            // 7. Limpiar formulario
            limpiarFormulario();
            
            return null;
        } catch (Exception e) {
            // 8. Manejo de errores
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Error", "No se pudo registrar la transferencia: " + e.getMessage()));
            throw new IllegalStateException("Error al registrar transferencia", e);
        }
    }

    /**
     * Obtiene el historial de transferencias del usuario autenticado.
     * 
     * @return List<Transferencia> lista de transferencias donde el usuario es origen o destino
     */
    public List<Transferencia> getTransferencias() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        return transferenciaService.listarTransferencias().stream()
            .filter(t -> ci.equals(t.getCuentaOrigen()) || ci.equals(t.getCuentaDestino()))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Valida los datos básicos de la transferencia.
     * 
     * @return boolean true si los datos son válidos, false en caso contrario
     */
    private boolean validarDatosTransferencia() {
        return cuentaOrigen != null && !cuentaOrigen.isEmpty() &&
               cuentaDestino != null && !cuentaDestino.isEmpty() &&
               !cuentaOrigen.equals(cuentaDestino) &&
               monto != null && monto.compareTo(BigDecimal.ZERO) > 0 &&
               descripcion != null && !descripcion.isEmpty();
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        cuentaOrigen = null;
        cuentaDestino = null;
        monto = null;
        descripcion = null;
    }

    // ==================== GETTERS Y SETTERS ==================== //

    /**
     * @return String cuenta origen de la transferencia
     */
    public String getCuentaOrigen() { 
        return cuentaOrigen; 
    }
    
    /**
     * @param cuentaOrigen String cuenta origen a establecer
     */
    public void setCuentaOrigen(String cuentaOrigen) { 
        this.cuentaOrigen = cuentaOrigen; 
    }

    /**
     * @return String cuenta destino de la transferencia
     */
    public String getCuentaDestino() { 
        return cuentaDestino; 
    }
    
    /**
     * @param cuentaDestino String cuenta destino a establecer
     */
    public void setCuentaDestino(String cuentaDestino) { 
        this.cuentaDestino = cuentaDestino; 
    }

    /**
     * @return BigDecimal monto de la transferencia
     */
    public BigDecimal getMonto() { 
        return monto; 
    }
    
    /**
     * @param monto BigDecimal monto a transferir
     */
    public void setMonto(BigDecimal monto) { 
        this.monto = monto; 
    }

    /**
     * @return String descripción de la transferencia
     */
    public String getDescripcion() { 
        return descripcion; 
    }
    
    /**
     * @param descripcion String descripción a establecer
     */
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }
}
