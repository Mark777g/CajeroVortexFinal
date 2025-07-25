package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.RetiroSinTarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RetiroSinTarjetaService {
    @PersistenceContext
    private EntityManager em;

    public void registrarRetiro(RetiroSinTarjeta retiro) {
        em.persist(retiro);
    }

    public List<RetiroSinTarjeta> listarRetiros() {
        TypedQuery<RetiroSinTarjeta> query = em.createQuery("SELECT r FROM RetiroSinTarjeta r ORDER BY r.fecha DESC", RetiroSinTarjeta.class);
        return query.getResultList();
    }
} 