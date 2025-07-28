/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 26/7/25
  Time: 14:42
*/

package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Deposito;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Servicio encargado de la gestión de operaciones relacionadas con la entidad {@link Deposito}.
 * Utiliza EJB de tipo Stateless para operaciones transaccionales con JPA.
 */
@Stateless
public class DepositoService {

    /**
     * EntityManager inyectado mediante la unidad de persistencia configurada.
     * Se utiliza para realizar operaciones sobre la base de datos.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Registra un nuevo depósito en la base de datos.
     *
     * @param deposito el objeto {@link Deposito} que se desea persistir.
     */
    public void registrarDeposito(Deposito deposito) {
        em.persist(deposito);
    }

    /**
     * Obtiene la lista de depósitos ordenados por fecha de forma descendente.
     *
     * @return una lista de objetos {@link Deposito}, ordenada desde el más reciente al más antiguo.
     */
    public List<Deposito> listarDepositos() {
        TypedQuery<Deposito> query = em.createQuery(
                "SELECT d FROM Deposito d ORDER BY d.fecha DESC", Deposito.class);
        return query.getResultList();
    }

    /**
     * Actualiza el estado de un depósito a "ACTIVO" dado su identificador.
     * Si el depósito no existe, no realiza ninguna acción.
     *
     * @param id el identificador único del depósito.
     */
    public void actualizarEstadoADepositoActivo(Long id) {
        Deposito deposito = em.find(Deposito.class, id);
        if (deposito != null) {
            deposito.setEstado("ACTIVO");
            em.merge(deposito);
        }
    }
}
