package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Tarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class TarjetaService {
    @PersistenceContext
    private EntityManager em;

    public void registrarTarjeta(Tarjeta tarjeta) {
        em.persist(tarjeta);
    }

    public List<Tarjeta> listarTarjetas() {
        TypedQuery<Tarjeta> query = em.createQuery("SELECT t FROM Tarjeta t ORDER BY t.fechaExpiracion DESC", Tarjeta.class);
        return query.getResultList();
    }

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