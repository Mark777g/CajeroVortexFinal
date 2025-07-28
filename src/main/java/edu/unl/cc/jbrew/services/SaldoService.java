package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Saldo;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;

/**
 * Servicio EJB para operaciones relacionadas con saldos de usuarios.
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Gestión de saldos de usuarios</li>
 *   <li>Operaciones básicas de suma/resta de saldo</li>
 *   <li>Creación y actualización de saldos</li>
 * </ul>
 * 
 * <p><strong>Alcance:</strong> Stateless (sin estado mantenido entre llamadas)</p>
 */
@Stateless
public class SaldoService {

    @PersistenceContext
    private EntityManager em;  // Inyecta el EntityManager para operaciones JPA

    /**
     * Obtiene el saldo asociado a una cédula de identidad.
     * 
     * @param ci Cédula de identidad del usuario
     * @return Saldo del usuario o null si no existe
     * @throws IllegalArgumentException si la cédula es nula o vacía
     */
    public Saldo obtenerPorCi(String ci) {
        if (ci == null || ci.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        
        try {
            TypedQuery<Saldo> query = em.createQuery(
                "SELECT s FROM Saldo s WHERE s.ci = :ci", 
                Saldo.class);
            return query.setParameter("ci", ci).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Crea un nuevo registro de saldo para un usuario.
     * 
     * @param ci Cédula de identidad del usuario
     * @throws IllegalArgumentException si la cédula es nula o vacía
     */
    public void crearSaldo(String ci) {
        if (ci == null || ci.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        
        Saldo saldo = new Saldo();
        saldo.setCi(ci);
        saldo.setSaldo(BigDecimal.ZERO);
        em.persist(saldo);
    }

    /**
     * Actualiza el saldo de un usuario a un valor específico.
     * 
     * @param ci Cédula de identidad del usuario
     * @param nuevoSaldo Nuevo valor del saldo
     * @return boolean true si la actualización fue exitosa, false si no se encontró el usuario
     * @throws IllegalArgumentException si la cédula es nula o el saldo es negativo
     */
    public boolean actualizarSaldo(String ci, BigDecimal nuevoSaldo) {
        if (ci == null || ci.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        if (nuevoSaldo == null || nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo");
        }
        
        Saldo saldo = obtenerPorCi(ci);
        if (saldo != null) {
            saldo.setSaldo(nuevoSaldo);
            em.merge(saldo);
            return true;
        }
        return false;
    }

    /**
     * Incrementa el saldo de un usuario.
     * 
     * @param ci Cédula de identidad del usuario
     * @param monto Cantidad a sumar al saldo
     * @return boolean true si la operación fue exitosa, false si no se encontró el usuario
     * @throws IllegalArgumentException si la cédula es nula o el monto es inválido
     */
    public boolean sumarSaldo(String ci, BigDecimal monto) {
        if (ci == null || ci.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        
        Saldo saldo = obtenerPorCi(ci);
        if (saldo != null) {
            saldo.setSaldo(saldo.getSaldo().add(monto));
            em.merge(saldo);
            return true;
        }
        return false;
    }

    /**
     * Reduce el saldo de un usuario si hay fondos suficientes.
     * 
     * @param ci Cédula de identidad del usuario
     * @param monto Cantidad a restar del saldo
     * @return boolean true si la operación fue exitosa, false si no hay fondos o no se encontró el usuario
     * @throws IllegalArgumentException si la cédula es nula o el monto es inválido
     */
    public boolean restarSaldo(String ci, BigDecimal monto) {
        if (ci == null || ci.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        
        Saldo saldo = obtenerPorCi(ci);
        if (saldo != null && saldo.getSaldo().compareTo(monto) >= 0) {
            saldo.setSaldo(saldo.getSaldo().subtract(monto));
            em.merge(saldo);
            return true;
        }
        return false;
    }

    /**
     * Transfiere saldo entre dos cuentas.
     * 
     * @param ciOrigen Cédula de la cuenta origen
     * @param ciDestino Cédula de la cuenta destino
     * @param monto Cantidad a transferir
     * @return boolean true si la transferencia fue exitosa
     * @throws IllegalArgumentException si alguna cédula es inválida o el monto es incorrecto
     */
    public boolean transferirSaldo(String ciOrigen, String ciDestino, BigDecimal monto) {
        if (ciOrigen == null || ciDestino == null || ciOrigen.equals(ciDestino)) {
            throw new IllegalArgumentException("Las cédulas deben ser válidas y diferentes");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        
        Saldo origen = obtenerPorCi(ciOrigen);
        Saldo destino = obtenerPorCi(ciDestino);
        
        if (origen != null && destino != null && origen.getSaldo().compareTo(monto) >= 0) {
            origen.setSaldo(origen.getSaldo().subtract(monto));
            destino.setSaldo(destino.getSaldo().add(monto));
            em.merge(origen);
            em.merge(destino);
            return true;
        }
        return false;
    }
}
