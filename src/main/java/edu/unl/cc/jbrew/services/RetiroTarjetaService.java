package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.RetiroTarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RetiroTarjetaService {
    @PersistenceContext
    private EntityManager em;

    public void registrarRetiroTarjeta(RetiroTarjeta retiro) {
        em.persist(retiro);
    }

    public List<RetiroTarjeta> listarRetirosTarjeta() {
        TypedQuery<RetiroTarjeta> query = em.createQuery("SELECT r FROM RetiroTarjeta r ORDER BY r.fecha DESC", RetiroTarjeta.class);
        return query.getResultList();
    }
} 