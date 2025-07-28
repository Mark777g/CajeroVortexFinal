/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 25/7/25
  Time: 16:48
*/

package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Entidad que representa el saldo asociado a un usuario identificado por su CI (cédula de identidad).
 * 
 * Esta clase se mapea a la tabla "saldos" en la base de datos.
 * Contiene un saldo que se inicializa en cero por defecto.
 */
@Entity
@Table(name = "saldos")
public class Saldo implements Serializable {

    /** Identificador único del saldo (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cédula de identidad del usuario, debe ser única y no nula. */
    @NotNull
    @Column(name = "ci", nullable = false, unique = true)
    private String ci;

    /** Valor numérico del saldo, no puede ser nulo. */
    @NotNull
    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;

    /**
     * Constructor por defecto que inicializa el saldo en cero.
     */
    public Saldo() {
        this.saldo = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}
