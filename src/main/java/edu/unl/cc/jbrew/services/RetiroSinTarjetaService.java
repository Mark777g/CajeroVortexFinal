package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.RetiroSinTarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Servicio EJB para operaciones relacionadas con retiros sin tarjeta.
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Registro de nuevos retiros sin tarjeta</li>
 *   <li>Consulta de historial de retiros</li>
 *   <li>Gestión de operaciones de retiro</li>
 * </ul>
 * 
 * <p><strong>Alcance:</strong> Stateless (sin estado mantenido entre llamadas)</p>
 */
@Stateless
public class RetiroSinTarjetaService {

    @PersistenceContext
    private EntityManager em;  // Inyecta el EntityManager para operaciones JPA

    /**
     * Registra un nuevo retiro sin tarjeta en el sistema.
     * 
     * @param retiro Entidad RetiroSinTarjeta a persistir
     * @throws IllegalArgumentException si el retiro es nulo o inválido
     */
    public void registrarRetiro(RetiroSinTarjeta retiro) {
        if (retiro == null) {
            throw new IllegalArgumentException("El retiro no puede ser nulo");
        }
        if (retiro.getCodigoRetiro() == null || retiro.getCodigoRetiro().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de retiro es requerido");
        }
        em.persist(retiro);
    }

    /**
     * Obtiene todos los retiros sin tarjeta ordenados por fecha descendente.
     * 
     * @return List<RetiroSinTarjeta> lista de retiros ordenados cronológicamente
     */
    public List<RetiroSinTarjeta> listarRetiros() {
        TypedQuery<RetiroSinTarjeta> query = em.createQuery(
            "SELECT r FROM RetiroSinTarjeta r ORDER BY r.fecha DESC", 
            RetiroSinTarjeta.class);
        return query.getResultList();
    }

    /**
     * Obtiene los retiros sin tarjeta de un usuario específico.
     * 
     * @param ci Cédula de identidad del usuario
     * @return List<RetiroSinTarjeta> lista de retiros del usuario ordenados por fecha
     */
    public List<RetiroSinTarjeta> listarRetirosPorUsuario(String ci) {
        if (ci == null || ci.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        
        TypedQuery<RetiroSinTarjeta> query = em.createQuery(
            "SELECT r FROM RetiroSinTarjeta r WHERE r.ci = :ci ORDER BY r.fecha DESC", 
            RetiroSinTarjeta.class);
        query.setParameter("ci", ci);
        return query.getResultList();
    }

    /**
     * Busca un retiro por su código único.
     * 
     * @param codigoRetiro Código único del retiro
     * @return RetiroSinTarjeta correspondiente al código, o null si no se encuentra
     */
    public RetiroSinTarjeta buscarPorCodigo(String codigoRetiro) {
        TypedQuery<RetiroSinTarjeta> query = em.createQuery(
            "SELECT r FROM RetiroSinTarjeta r WHERE r.codigoRetiro = :codigo", 
            RetiroSinTarjeta.class);
        query.setParameter("codigo", codigoRetiro);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
