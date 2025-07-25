package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Transferencia;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class TransferenciaService {
    @PersistenceContext
    private EntityManager em;

    public void registrarTransferencia(Transferencia transferencia) {
        em.persist(transferencia);
    }

    public List<Transferencia> listarTransferencias() {
        TypedQuery<Transferencia> query = em.createQuery("SELECT t FROM Transferencia t ORDER BY t.fecha DESC", Transferencia.class);
        return query.getResultList();
    }
} 