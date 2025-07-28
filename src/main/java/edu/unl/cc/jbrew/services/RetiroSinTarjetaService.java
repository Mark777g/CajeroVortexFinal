/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 26/7/25
  Time: 15:03
*/

package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.RetiroSinTarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con la entidad {@link RetiroSinTarjeta}.
 * Implementa un EJB Stateless para manejar transacciones y operaciones con JPA.
 */
@Stateless
public class RetiroSinTarjetaService {

    /**
     * EntityManager inyectado mediante la unidad de persistencia configurada.
     * Permite realizar operaciones de persistencia sobre la base de datos.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Registra un nuevo retiro sin tarjeta en la base de datos.
     *
     * @param retiro el objeto {@link RetiroSinTarjeta} que se desea persistir.
     */
    public void registrarRetiro(RetiroSinTarjeta retiro) {
        em.persist(retiro);
    }

    /**
     * Obtiene la lista de retiros sin tarjeta ordenados por fecha en orden descendente.
     *
     * @return una lista de objetos {@link RetiroSinTarjeta}, ordenada desde el más reciente al más antiguo.
     */
    public List<RetiroSinTarjeta> listarRetiros() {
        TypedQuery<RetiroSinTarjeta> query = em.createQuery(
                "SELECT r FROM RetiroSinTarjeta r ORDER BY r.fecha DESC", RetiroSinTarjeta.class);
        return query.getResultList();
    }
}
