/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 26/7/25
  Time: 21:02
*/


package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Saldo;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con la entidad {@link Saldo}.
 * Permite consultar, crear y actualizar saldos asociados a clientes mediante JPA.
 */
@Stateless
public class SaldoService {

    /**
     * EntityManager inyectado para interactuar con la base de datos.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Obtiene el saldo asociado a un cliente por su número de cédula (CI).
     *
     * @param ci número de cédula del cliente.
     * @return el objeto {@link Saldo} correspondiente, o {@code null} si no existe.
     */
    public Saldo obtenerPorCi(String ci) {
        try {
            TypedQuery<Saldo> query = em.createQuery(
                    "SELECT s FROM Saldo s WHERE s.ci = :ci", Saldo.class);
            return query.setParameter("ci", ci).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Crea un nuevo saldo con valor cero para un cliente identificado por su CI.
     *
     * @param ci número de cédula del cliente.
     */
    public void crearSaldo(String ci) {
        Saldo saldo = new Saldo();
        saldo.setCi(ci);
        saldo.setSaldo(BigDecimal.ZERO);
        em.persist(saldo);
    }

    /**
     * Actualiza el saldo de un cliente con un nuevo valor.
     *
     * @param ci número de cédula del cliente.
     * @param nuevoSaldo nuevo valor del saldo.
     */
    public void actualizarSaldo(String ci, BigDecimal nuevoSaldo) {
        Saldo saldo = obtenerPorCi(ci);
        if (saldo != null) {
            saldo.setSaldo(nuevoSaldo);
            em.merge(saldo);
        }
    }

    /**
     * Suma un monto al saldo actual del cliente.
     *
     * @param ci número de cédula del cliente.
     * @param monto cantidad a sumar al saldo.
     */
    public void sumarSaldo(String ci, BigDecimal monto) {
        Saldo saldo = obtenerPorCi(ci);
        if (saldo != null && monto != null) {
            saldo.setSaldo(saldo.getSaldo().add(monto));
            em.merge(saldo);
        }
    }

    /**
     * Resta un monto al saldo actual del cliente, si hay fondos suficientes.
     *
     * @param ci número de cédula del cliente.
     * @param monto cantidad a restar del saldo.
     * @return {@code true} si la operación se realizó con éxito, {@code false} si no había saldo suficiente.
     */
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
