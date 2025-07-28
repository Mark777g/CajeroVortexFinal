package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Transferencia;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con la entidad {@link Transferencia}.
 * Permite registrar transferencias y obtener el listado de todas las realizadas.
 */
@Stateless
public class TransferenciaService {

    /**
     * EntityManager inyectado para interactuar con la base de datos mediante JPA.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Registra una nueva transferencia en la base de datos.
     *
     * @param transferencia el objeto {@link Transferencia} que se desea persistir.
     */
    public void registrarTransferencia(Transferencia transferencia) {
        em.persist(transferencia);
    }

    /**
     * Obtiene la lista de transferencias ordenadas por fecha en orden descendente.
     *
     * @return una lista de objetos {@link Transferencia}, desde la más reciente a la más antigua.
     */
    public List<Transferencia> listarTransferencias() {
        TypedQuery<Transferencia> query = em.createQuery(
                "SELECT t FROM Transferencia t ORDER BY t.fecha DESC", Transferencia.class);
        return query.getResultList();
    }
}
