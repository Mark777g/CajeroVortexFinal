package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.RetiroTarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Servicio EJB para operaciones relacionadas con retiros realizados con tarjeta.
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Registro de nuevos retiros con tarjeta</li>
 *   <li>Consulta de historial de retiros</li>
 *   <li>Gestión de operaciones de retiro con tarjeta</li>
 * </ul>
 * 
 * <p><strong>Alcance:</strong> Stateless (sin estado mantenido entre llamadas)</p>
 */
@Stateless
public class RetiroTarjetaService {

    @PersistenceContext
    private EntityManager em;  // Inyecta el EntityManager para operaciones JPA

    /**
     * Registra un nuevo retiro con tarjeta en el sistema.
     * 
     * @param retiro Entidad RetiroTarjeta a persistir
     * @throws IllegalArgumentException si el retiro es nulo o inválido
     */
    public void registrarRetiroTarjeta(RetiroTarjeta retiro) {
        if (retiro == null) {
            throw new IllegalArgumentException("El retiro no puede ser nulo");
        }
        if (retiro.getNumeroTarjeta() == null || retiro.getNumeroTarjeta().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de tarjeta es requerido");
        }
        em.persist(retiro);
    }

    /**
     * Obtiene todos los retiros con tarjeta ordenados por fecha descendente.
     * 
     * @return List<RetiroTarjeta> lista de retiros ordenados cronológicamente
     */
    public List<RetiroTarjeta> listarRetirosTarjeta() {
        TypedQuery<RetiroTarjeta> query = em.createQuery(
            "SELECT r FROM RetiroTarjeta r ORDER BY r.fecha DESC", 
            RetiroTarjeta.class);
        return query.getResultList();
    }

    /**
     * Obtiene los retiros con tarjeta de un usuario específico.
     * 
     * @param ci Cédula de identidad del usuario
     * @return List<RetiroTarjeta> lista de retiros del usuario ordenados por fecha
     */
    public List<RetiroTarjeta> listarRetirosTarjetaPorUsuario(String ci) {
        if (ci == null || ci.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        
        TypedQuery<RetiroTarjeta> query = em.createQuery(
            "SELECT r FROM RetiroTarjeta r WHERE r.ci = :ci ORDER BY r.fecha DESC", 
            RetiroTarjeta.class);
        query.setParameter("ci", ci);
        return query.getResultList();
    }

    /**
     * Busca retiros por número de tarjeta (últimos 4 dígitos).
     * 
     * @param ultimosCuatroDigitos Últimos 4 dígitos de la tarjeta
     * @return List<RetiroTarjeta> lista de retiros asociados a la tarjeta
     */
    public List<RetiroTarjeta> buscarPorTarjeta(String ultimosCuatroDigitos) {
        if (ultimosCuatroDigitos == null || ultimosCuatroDigitos.length() != 4) {
            throw new IllegalArgumentException("Debe proporcionar los últimos 4 dígitos de la tarjeta");
        }
        
        TypedQuery<RetiroTarjeta> query = em.createQuery(
            "SELECT r FROM RetiroTarjeta r WHERE r.numeroTarjeta LIKE :digitos ORDER BY r.fecha DESC", 
            RetiroTarjeta.class);
        query.setParameter("digitos", "%" + ultimosCuatroDigitos);
        return query.getResultList();
    }
}
