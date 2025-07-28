package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Tarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Servicio EJB para operaciones relacionadas con tarjetas bancarias.
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Registro de nuevas tarjetas</li>
 *   <li>Validación de tarjetas</li>
 *   <li>Consulta de información de tarjetas</li>
 *   <li>Gestión del estado de tarjetas</li>
 * </ul>
 * 
 * <p><strong>Alcance:</strong> Stateless (sin estado mantenido entre llamadas)</p>
 */
@Stateless
public class TarjetaService {

    @PersistenceContext
    private EntityManager em;  // Inyecta el EntityManager para operaciones JPA

    /**
     * Registra una nueva tarjeta en el sistema.
     * 
     * @param tarjeta Entidad Tarjeta a persistir
     * @throws IllegalArgumentException si la tarjeta es nula o inválida
     */
    public void registrarTarjeta(Tarjeta tarjeta) {
        if (tarjeta == null) {
            throw new IllegalArgumentException("La tarjeta no puede ser nula");
        }
        if (tarjeta.getNumero() == null || tarjeta.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de tarjeta es requerido");
        }
        if (tarjeta.getCvc() == null || tarjeta.getCvc().trim().isEmpty()) {
            throw new IllegalArgumentException("El código CVC es requerido");
        }
        if (tarjeta.getFechaExpiracion() == null || tarjeta.getFechaExpiracion().before(new Date())) {
            throw new IllegalArgumentException("La fecha de expiración debe ser válida");
        }
        
        // Enmascarar número de tarjeta antes de persistir (excepto últimos 4 dígitos)
        String numeroEnmascarado = enmascararNumeroTarjeta(tarjeta.getNumero());
        tarjeta.setNumero(numeroEnmascarado);
        
        em.persist(tarjeta);
    }

    /**
     * Obtiene todas las tarjetas ordenadas por fecha de expiración descendente.
     * 
     * @return List<Tarjeta> lista de tarjetas registradas
     */
    public List<Tarjeta> listarTarjetas() {
        TypedQuery<Tarjeta> query = em.createQuery(
            "SELECT t FROM Tarjeta t ORDER BY t.fechaExpiracion DESC", 
            Tarjeta.class);
        return query.getResultList();
    }

    /**
     * Valida una tarjeta según sus datos y estado.
     * 
     * @param numero Número de tarjeta (completo)
     * @param cvc Código de seguridad
     * @param fechaExpiracion Fecha de expiración
     * @return Tarjeta válida o null si no cumple con los requisitos
     */
    public Tarjeta validarTarjeta(String numero, String cvc, Date fechaExpiracion) {
        if (numero == null || cvc == null || fechaExpiracion == null) {
            return null;
        }
        
        TypedQuery<Tarjeta> query = em.createQuery(
            "SELECT t FROM Tarjeta t WHERE t.numero = :numero AND t.cvc = :cvc " +
            "AND t.fechaExpiracion = :fechaExpiracion AND t.estado = 'ACTIVA'",
            Tarjeta.class);
        
        query.setParameter("numero", enmascararNumeroTarjeta(numero));
        query.setParameter("cvc", cvc);
        query.setParameter("fechaExpiracion", fechaExpiracion);
        
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Busca una tarjeta por sus datos básicos.
     * 
     * @param numero Número de tarjeta
     * @param cvc Código de seguridad
     * @param fechaExpiracion Fecha de expiración
     * @return Tarjeta encontrada o null si no existe
     */
    public Tarjeta buscarTarjetaPorDatos(String numero, String cvc, Date fechaExpiracion) {
        if (numero == null || cvc == null || fechaExpiracion == null) {
            return null;
        }
        
        TypedQuery<Tarjeta> query = em.createQuery(
            "SELECT t FROM Tarjeta t WHERE t.numero = :numero AND t.cvc = :cvc " +
            "AND t.fechaExpiracion = :fechaExpiracion",
            Tarjeta.class);
        
        query.setParameter("numero", enmascararNumeroTarjeta(numero));
        query.setParameter("cvc", cvc);
        query.setParameter("fechaExpiracion", fechaExpiracion);
        
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene las tarjetas de un usuario específico.
     * 
     * @param ci Cédula de identidad del titular
     * @return List<Tarjeta> tarjetas asociadas al usuario
     */
    public List<Tarjeta> listarTarjetasPorUsuario(String ci) {
        if (ci == null || ci.trim().isEmpty()) {
            return List.of();
        }
        
        TypedQuery<Tarjeta> query = em.createQuery(
            "SELECT t FROM Tarjeta t WHERE t.ci = :ci ORDER BY t.fechaExpiracion DESC",
            Tarjeta.class);
        query.setParameter("ci", ci);
        return query.getResultList();
    }

    /**
     * Actualiza el estado de una tarjeta.
     * 
     * @param id Identificador de la tarjeta
     * @param nuevoEstado Nuevo estado a asignar (ACTIVA, BLOQUEADA, etc.)
     * @return boolean true si la actualización fue exitosa
     */
    public boolean actualizarEstadoTarjeta(Long id, String nuevoEstado) {
        Tarjeta tarjeta = em.find(Tarjeta.class, id);
        if (tarjeta != null) {
            tarjeta.setEstado(nuevoEstado);
            em.merge(tarjeta);
            return true;
        }
        return false;
    }

    /**
     * Enmascara el número de tarjeta, mostrando solo los últimos 4 dígitos.
     * 
     * @param numero Número completo de tarjeta
     * @return String número enmascarado (ej. **** **** **** 1234)
     */
    private String enmascararNumeroTarjeta(String numero) {
        if (numero == null || numero.length() < 4) {
            return numero;
        }
        String ultimosCuatro = numero.substring(numero.length() - 4);
        return "**** **** **** " + ultimosCuatro;
    }
}
