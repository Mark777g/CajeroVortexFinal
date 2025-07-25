package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Deposito;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class DepositoService {
    @PersistenceContext
    private EntityManager em;

    public void registrarDeposito(Deposito deposito) {
        em.persist(deposito);
    }

    public List<Deposito> listarDepositos() {
        TypedQuery<Deposito> query = em.createQuery("SELECT d FROM Deposito d ORDER BY d.fecha DESC", Deposito.class);
        return query.getResultList();
    }

    public void actualizarEstadoADepositoActivo(Long id) {
        Deposito deposito = em.find(Deposito.class, id);
        if (deposito != null) {
            deposito.setEstado("ACTIVO");
            em.merge(deposito);
        }
    }
} 