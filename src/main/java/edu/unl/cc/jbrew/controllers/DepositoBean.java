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

/**
 * Managed Bean para gestionar operaciones relacionadas con depósitos.
 * Controlador entre la vista y el servicio de depósitos.
 * 
 * <p>Alcance: RequestScoped (nueva instancia por cada petición HTTP)</p>
 * 
 * @see DepositoService
 * @see InicioBean
 * @see AuthenticationBean
 */
@Named
@RequestScoped
public class DepositoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // Campos del formulario
    private String ci;               // Cédula de identidad asociada al depósito
    private BigDecimal monto;        // Cantidad monetaria del depósito
    private String descripcion;      // Descripción/observación del depósito

    // Dependencias inyectadas
    @Inject
    private DepositoService depositoService;  // Servicio para operaciones con depósitos
    
    @Inject
    private InicioBean inicioBean;            // Bean para actualizar información de inicio
    
    @Inject
    private AuthenticationBean authenticationBean; // Bean de autenticación para obtener usuario actual

    /**
     * Registra un nuevo depósito en el sistema.
     * 
     * @return String navegación (siempre null para permanecer en la misma vista)
     */
    public String registrarDeposito() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // 1. Crear entidad Deposito
            Deposito d = new Deposito();
            d.setCi(ci);
            d.setMonto(monto);
            d.setDescripcion(descripcion);
            
            // 2. Persistir el depósito
            depositoService.registrarDeposito(d);
            
            // 3. Actualizar estado a ACTIVO
            depositoService.actualizarEstadoADepositoActivo(d.getId());
            
            // 4. Actualizar total en InicioBean
            inicioBean.sumarDeposito(monto);
            
            // 5. Mostrar mensaje de éxito
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", 
                "Depósito registrado correctamente."));
            
            // 6. Limpiar formulario
            limpiarFormulario();
            
            return null;
        } catch (Exception e) {
            // Manejo de errores
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                "No se pudo registrar el depósito: " + e.getMessage()));
            return null;
        }
    }

    /**
     * Obtiene la lista de depósitos del usuario autenticado.
     * 
     * @return List<Deposito> lista filtrada de depósitos o lista vacía si no hay usuario
     */
    public List<Deposito> getDepositos() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        
        return depositoService.listarDepositos().stream()
            .filter(d -> ci.equals(d.getCi()))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        ci = null;
        monto = null;
        descripcion = null;
    }

    // ============== GETTERS Y SETTERS ============== //
    
    /**
     * @return String cédula de identidad asociada
     */
    public String getCi() { return ci; }
    
    /**
     * @param ci String cédula de identidad a establecer
     */
    public void setCi(String ci) { this.ci = ci; }
    
    /**
     * @return BigDecimal monto del depósito
     */
    public BigDecimal getMonto() { return monto; }
    
    /**
     * @param monto BigDecimal cantidad a establecer
     */
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    
    /**
     * @return String descripción del depósito
     */
    public String getDescripcion() { return descripcion; }
    
    /**
     * @param descripcion String descripción a establecer
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
