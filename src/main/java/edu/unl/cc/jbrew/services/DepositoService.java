package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Deposito;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Servicio EJB para operaciones relacionadas con depósitos.
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Registro de nuevos depósitos</li>
 *   <li>Consulta de historial de depósitos</li>
 *   <li>Gestión de estados de depósitos</li>
 * </ul>
 * 
 * <p><strong>Alcance:</strong> Stateless (sin estado mantenido entre llamadas)</p>
 */
@Stateless
public class DepositoService {

    @PersistenceContext
    private EntityManager em;  // Inyecta el EntityManager para operaciones JPA

    /**
     * Registra un nuevo depósito en el sistema.
     * 
     * @param deposito Entidad Deposito a persistir
     * @throws IllegalArgumentException si el depósito es nulo
     */
    public void registrarDeposito(Deposito deposito) {
        if (deposito == null) {
            throw new IllegalArgumentException("El depósito no puede ser nulo");
        }
        em.persist(deposito);
    }

    /**
     * Obtiene todos los depósitos ordenados por fecha descendente.
     * 
     * @return List<Deposito> lista de depósitos ordenados
     */
    public List<Deposito> listarDepositos() {
        TypedQuery<Deposito> query = em.createQuery(
            "SELECT d FROM Deposito d ORDER BY d.fecha DESC", 
            Deposito.class);
        return query.getResultList();
    }

    /**
     * Actualiza el estado de un depósito a "ACTIVO".
     * 
     * @param id Identificador del depósito a actualizar
     * @return boolean true si la actualización fue exitosa, false si no se encontró el depósito
     */
    public boolean actualizarEstadoADepositoActivo(Long id) {
        Deposito deposito = em.find(Deposito.class, id);
        if (deposito != null) {
            deposito.setEstado("ACTIVO");
            em.merge(deposito);
            return true;
        }
        return false;
    }

    /**
     * Obtiene los depósitos de un usuario específico.
     * 
     * @param ci Cédula de identidad del usuario
     * @return List<Deposito> lista de depósitos del usuario ordenados por fecha
     */
    public List<Deposito> listarDepositosPorUsuario(String ci) {
        TypedQuery<Deposito> query = em.createQuery(
            "SELECT d FROM Deposito d WHERE d.ci = :ci ORDER BY d.fecha DESC", 
            Deposito.class);
        query.setParameter("ci", ci);
        return query.getResultList();
    }
}
