/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 26/7/25
  Time: 20:10
*/


package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Tarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con la entidad {@link Tarjeta}.
 * Permite registrar, listar y validar tarjetas utilizando JPA.
 */
@Stateless
public class TarjetaService {

    /**
     * EntityManager inyectado para interactuar con la base de datos mediante JPA.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Registra una nueva tarjeta en la base de datos.
     *
     * @param tarjeta el objeto {@link Tarjeta} que se desea persistir.
     */
    public void registrarTarjeta(Tarjeta tarjeta) {
        em.persist(tarjeta);
    }

    /**
     * Obtiene la lista de todas las tarjetas, ordenadas por la fecha de expiración en orden descendente.
     *
     * @return una lista de objetos {@link Tarjeta}.
     */
    public List<Tarjeta> listarTarjetas() {
        TypedQuery<Tarjeta> query = em.createQuery(
                "SELECT t FROM Tarjeta t ORDER BY t.fechaExpiracion DESC", Tarjeta.class);
        return query.getResultList();
    }

    /**
     * Valida una tarjeta verificando que exista en la base de datos y que esté activa,
     * comparando número, CVC y fecha de expiración.
     *
     * @param numero número de la tarjeta.
     * @param cvc código de seguridad de la tarjeta.
     * @param fechaExpiracion fecha de expiración de la tarjeta.
     * @return el objeto {@link Tarjeta} si es válida y activa; {@code null} en caso contrario.
     */
    public Tarjeta validarTarjeta(String numero, String cvc, java.util.Date fechaExpiracion) {
        TypedQuery<Tarjeta> query = em.createQuery(
            "SELECT t FROM Tarjeta t WHERE t.numero = :numero AND t.cvc = :cvc AND t.fechaExpiracion = :fechaExpiracion AND t.estado = 'ACTIVA'",
            Tarjeta.class);
        query.setParameter("numero", numero);
        query.setParameter("cvc", cvc);
        query.setParameter("fechaExpiracion", fechaExpiracion);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Busca una tarjeta en la base de datos que coincida con el número, CVC y fecha de expiración
     * y que esté activa.
     *
     * @param numero número de la tarjeta.
     * @param cvc código de seguridad de la tarjeta.
     * @param fechaExpiracion fecha de expiración de la tarjeta.
     * @return el objeto {@link Tarjeta} si se encuentra y está activa; {@code null} si no existe.
     */
    public Tarjeta buscarTarjetaPorDatos(String numero, String cvc, java.util.Date fechaExpiracion) {
        TypedQuery<Tarjeta> query = em.createQuery(
            "SELECT t FROM Tarjeta t WHERE t.numero = :numero AND t.cvc = :cvc AND t.fechaExpiracion = :fechaExpiracion AND t.estado = 'ACTIVA'",
            Tarjeta.class);
        query.setParameter("numero", numero);
        query.setParameter("cvc", cvc);
        query.setParameter("fechaExpiracion", fechaExpiracion);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
