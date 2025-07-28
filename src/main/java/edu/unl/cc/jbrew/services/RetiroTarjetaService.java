/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 26/7/25
  Time: 15:54
*/

package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.RetiroTarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con la entidad {@link RetiroTarjeta}.
 * Utiliza un EJB de tipo Stateless para manejar las operaciones transaccionales con JPA.
 */
@Stateless
public class RetiroTarjetaService {

    /**
     * EntityManager inyectado mediante la unidad de persistencia configurada.
     * Permite realizar operaciones CRUD sobre la base de datos.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Registra un nuevo retiro con tarjeta en la base de datos.
     *
     * @param retiro el objeto {@link RetiroTarjeta} que se desea persistir.
     */
    public void registrarRetiroTarjeta(RetiroTarjeta retiro) {
        em.persist(retiro);
    }

    /**
     * Obtiene la lista de retiros con tarjeta ordenados por fecha en orden descendente.
     *
     * @return una lista de objetos {@link RetiroTarjeta}, ordenada desde el más reciente al más antiguo.
     */
    public List<RetiroTarjeta> listarRetirosTarjeta() {
        TypedQuery<RetiroTarjeta> query = em.createQuery(
                "SELECT r FROM RetiroTarjeta r ORDER BY r.fecha DESC", RetiroTarjeta.class);
        return query.getResultList();
    }
}
