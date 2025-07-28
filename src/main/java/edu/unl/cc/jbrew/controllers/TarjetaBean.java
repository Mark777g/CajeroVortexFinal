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

/**
 * Managed Bean para la gestión de tarjetas asociadas a usuarios.
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Registro de nuevas tarjetas bancarias</li>
 *   <li>Consulta de tarjetas asociadas al usuario</li>
 *   <li>Validación básica de datos de tarjetas</li>
 * </ul>
 * 
 * <p><strong>Alcance:</strong> RequestScoped (nueva instancia por cada petición HTTP)</p>
 */
@Named
@RequestScoped
public class TarjetaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // Campos del formulario
    private String ci;                  // Cédula de identidad del titular
    private String numero;              // Número de tarjeta (sin formato)
    private String tipo;                // Tipo de tarjeta (Visa, MasterCard, etc.)
    private Date fechaExpiracion;       // Fecha de expiración de la tarjeta
    private String nombreTitular;       // Nombre del titular como aparece en la tarjeta
    private String cvc;                 // Código de seguridad (3-4 dígitos)

    // Dependencias inyectadas
    @Inject
    private TarjetaService tarjetaService;      // Servicio para operaciones con tarjetas
    
    @Inject
    private AuthenticationBean authenticationBean; // Bean de autenticación

    /**
     * Registra una nueva tarjeta asociada al usuario.
     * 
     * @return String navegación (siempre null para permanecer en la misma vista)
     * @throws IllegalStateException si ocurre un error durante el registro
     */
    public String registrarTarjeta() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // 1. Validar datos básicos
            if (!validarDatosTarjeta()) {
                context.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "Datos de la tarjeta incompletos o inválidos"));
                return null;
            }

            // 2. Crear entidad Tarjeta
            Tarjeta t = new Tarjeta();
            t.setCi(ci);
            t.setNumero(numero.replaceAll("\\s+", "")); // Eliminar espacios
            t.setTipo(tipo);
            t.setFechaExpiracion(fechaExpiracion);
            t.setNombreTitular(nombreTitular);
            t.setCvc(cvc);

            // 3. Persistir la tarjeta
            tarjetaService.registrarTarjeta(t);
            
            // 4. Mostrar mensaje de éxito
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Éxito", "Tarjeta registrada correctamente."));
            
            // 5. Limpiar formulario
            limpiarFormulario();
            
            return null;
        } catch (Exception e) {
            // 6. Manejo de errores
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Error", "No se pudo registrar la tarjeta: " + e.getMessage()));
            throw new IllegalStateException("Error al registrar tarjeta", e);
        }
    }

    /**
     * Obtiene las tarjetas asociadas al usuario autenticado.
     * 
     * @return List<Tarjeta> lista de tarjetas del usuario o lista vacía si no hay usuario
     */
    public List<Tarjeta> getTarjetas() {
        String ci = authenticationBean.getUsername();
        if (ci == null) {
            return java.util.Collections.emptyList();
        }
        return tarjetaService.listarTarjetas().stream()
            .filter(t -> ci.equals(t.getCi()))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Valida los datos básicos de la tarjeta antes de registrar.
     * 
     * @return boolean true si los datos son válidos, false en caso contrario
     */
    private boolean validarDatosTarjeta() {
        return ci != null && !ci.isEmpty() &&
               numero != null && numero.replaceAll("\\s+", "").length() >= 13 &&
               tipo != null && !tipo.isEmpty() &&
               fechaExpiracion != null &&
               nombreTitular != null && !nombreTitular.isEmpty() &&
               cvc != null && cvc.matches("\\d{3,4}");
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        ci = null;
        numero = null;
        tipo = null;
        fechaExpiracion = null;
        nombreTitular = null;
        cvc = null;
    }

    // ==================== GETTERS Y SETTERS ==================== //

    /**
     * @return String cédula de identidad asociada
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
     * @return String número de tarjeta (sin formato)
     */
    public String getNumero() { 
        return numero; 
    }
    
    /**
     * @param numero String número de tarjeta a establecer
     */
    public void setNumero(String numero) { 
        this.numero = numero; 
    }

    /**
     * @return String tipo de tarjeta (Visa, MasterCard, etc.)
     */
    public String getTipo() { 
        return tipo; 
    }
    
    /**
     * @param tipo String tipo de tarjeta a establecer
     */
    public void setTipo(String tipo) { 
        this.tipo = tipo; 
    }

    /**
     * @return Date fecha de expiración de la tarjeta
     */
    public Date getFechaExpiracion() { 
        return fechaExpiracion; 
    }
    
    /**
     * @param fechaExpiracion Date fecha de expiración a establecer
     */
    public void setFechaExpiracion(Date fechaExpiracion) { 
        this.fechaExpiracion = fechaExpiracion; 
    }

    /**
     * @return String nombre del titular de la tarjeta
     */
    public String getNombreTitular() { 
        return nombreTitular; 
    }
    
    /**
     * @param nombreTitular String nombre del titular a establecer
     */
    public void setNombreTitular(String nombreTitular) { 
        this.nombreTitular = nombreTitular; 
    }

    /**
     * @return String código de seguridad (CVC/CVV)
     */
    public String getCvc() { 
        return cvc; 
    }
    
    /**
     * @param cvc String código de seguridad a establecer
     */
    public void setCvc(String cvc) { 
        this.cvc = cvc; 
    }
}
