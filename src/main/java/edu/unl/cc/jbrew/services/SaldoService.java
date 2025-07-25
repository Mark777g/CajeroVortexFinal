package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Saldo;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;

@Stateless
public class SaldoService {
    @PersistenceContext
    private EntityManager em;

    public Saldo obtenerPorCi(String ci) {
        try {
            TypedQuery<Saldo> query = em.createQuery("SELECT s FROM Saldo s WHERE s.ci = :ci", Saldo.class);
            return query.setParameter("ci", ci).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void crearSaldo(String ci) {
        Saldo saldo = new Saldo();
        saldo.setCi(ci);
        saldo.setSaldo(BigDecimal.ZERO);
        em.persist(saldo);
    }

    public void actualizarSaldo(String ci, BigDecimal nuevoSaldo) {
        Saldo saldo = obtenerPorCi(ci);
        if (saldo != null) {
            saldo.setSaldo(nuevoSaldo);
            em.merge(saldo);
        }
    }

    public void sumarSaldo(String ci, BigDecimal monto) {
        Saldo saldo = obtenerPorCi(ci);
        if (saldo != null && monto != null) {
            saldo.setSaldo(saldo.getSaldo().add(monto));
            em.merge(saldo);
        }
    }

    public boolean restarSaldo(String ci, BigDecimal monto) {
        Saldo saldo = obtenerPorCi(ci);
        if (saldo != null && monto != null && saldo.getSaldo().compareTo(monto) >= 0) {
            saldo.setSaldo(saldo.getSaldo().subtract(monto));
            em.merge(saldo);
            return true;
        }
        return false;
    }
} 