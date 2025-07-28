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

/**
 * Managed Bean para gestionar operaciones de retiro sin tarjeta.
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Registro de nuevos retiros sin tarjeta</li>
 *   <li>Generación de códigos de retiro únicos</li>
 *   <li>Validación de saldo disponible</li>
 *   <li>Consulta de historial de retiros</li>
 * </ul>
 * 
 * <p><strong>Alcance:</strong> RequestScoped (nueva instancia por cada petición HTTP)</p>
 */
@Named
@RequestScoped
public class RetiroSinTarjetaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // Campos del formulario
    private String ci;                  // Cédula de identidad del usuario
    private BigDecimal monto;           // Monto a retirar
    private String codigoRetiro;        // Código de retiro (opcional)

    // Dependencias inyectadas
    @Inject
    private RetiroSinTarjetaService retiroService;  // Servicio para operaciones de retiro
    
    @Inject
    private InicioBean inicioBean;                  // Bean para verificar/actualizar saldo
    
    @Inject
    private AuthenticationBean authenticationBean;  // Bean de autenticación

    /**
     * Registra un nuevo retiro sin tarjeta.
     * 
     * @return String navegación (siempre null para permanecer en la misma vista)
     * @throws IllegalStateException si ocurre un error durante el registro
     */
    public String registrarRetiro() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // 1. Verificar saldo suficiente
            if (!inicioBean.restarRetiro(monto)) {
                context.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "Saldo insuficiente para el retiro."));
                return null;
            }

            // 2. Crear entidad RetiroSinTarjeta
            RetiroSinTarjeta r = new RetiroSinTarjeta();
            r.setCi(ci);
            r.setMonto(monto);
            
            // 3. Generar código único si no se proporcionó uno
            if (codigoRetiro == null || codigoRetiro.trim().isEmpty()) {
                r.setCodigoRetiro(UUID.randomUUID().toString());
            } else {
                r.setCodigoRetiro(codigoRetiro);
            }

            // 4. Persistir el retiro
            retiroService.registrarRetiro(r);
            
            // 5. Mostrar mensaje de éxito
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Éxito", "Retiro registrado correctamente."));
            
            // 6. Limpiar formulario
            limpiarFormulario();
            
            return null;
        } catch (Exception e) {
            // 7. Manejo de errores
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Error", "No se pudo registrar el retiro: " + e.getMessage()));
            throw new IllegalStateException("Error al registrar retiro", e);
        }
    }

    /**
     * Obtiene el historial de retiros del usuario autenticado.
     * 
     * @return List<RetiroSinTarjeta> lista de retiros del usuario o lista vacía si no hay usuario
     */
    public List<RetiroSinTarjeta> getRetiros() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        return retiroService.listarRetiros().stream()
            .filter(r -> ci.equals(r.getCi()))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        ci = null;
        monto = null;
        codigoRetiro = null;
    }

    // ==================== GETTERS Y SETTERS ==================== //

    /**
     * @return String cédula de identidad asociada al retiro
     */
    public String getCi() { 
        return ci; 
    }
    
    /**
     * @param ci String cédula de identidad a establecer
     */
    public void setCi(String ci) { 
        this.ci = ci; 
    }

    /**
     * @return BigDecimal monto del retiro
     */
    public BigDecimal getMonto() { 
        return monto; 
    }
    
    /**
     * @param monto BigDecimal cantidad a retirar
     */
    public void setMonto(BigDecimal monto) { 
        this.monto = monto; 
    }

    /**
     * @return String código de retiro generado/proporcionado
     */
    public String getCodigoRetiro() { 
        return codigoRetiro; 
    }
    
    /**
     * @param codigoRetiro String código de retiro a establecer
     */
    public void setCodigoRetiro(String codigoRetiro) { 
        this.codigoRetiro = codigoRetiro; 
    }
}
