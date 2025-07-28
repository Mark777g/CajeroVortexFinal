/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 25/7/25
  Time: 15:50
*/

package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Representa un depósito de dinero realizado por un cliente en el sistema.
 * 
 * Esta entidad se mapea a la tabla `depositos` en la base de datos y almacena
 * información relevante del depósito como el CI del cliente, el monto, la fecha,
 * una descripción opcional y el estado del depósito.
 * 
 * Al crearse un nuevo objeto de tipo Deposito, se asigna automáticamente
 * la fecha actual y el estado por defecto es "PENDIENTE".
 * 
 * Campos:
 * <ul>
 *   <li><b>id</b>: Identificador único del depósito (autogenerado).</li>
 *   <li><b>ci</b>: Cédula de identidad del cliente.</li>
 *   <li><b>monto</b>: Monto depositado.</li>
 *   <li><b>fecha</b>: Fecha y hora del depósito.</li>
 *   <li><b>descripcion</b>: Descripción opcional del depósito.</li>
 *   <li><b>estado</b>: Estado actual del depósito (por ejemplo, "PENDIENTE").</li>
 * </ul>
 * 
 * Reglas de validación:
 * <ul>
 *   <li><b>ci</b>, <b>monto</b> y <b>fecha</b> son campos obligatorios.</li>
 * </ul>
 * 
 * Esta clase implementa {@link java.io.Serializable} para permitir su persistencia y transporte.
 * 
 * @author 
 */
@Entity
@Table(name = "depositos")
public class Deposito implements Serializable {

    /** Identificador único del depósito (clave primaria) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cédula de identidad del cliente que realiza el depósito */
    @NotNull
    @Column(name = "ci", nullable = false)
    private String ci;

    /** Monto depositado */
    @NotNull
    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    /** Fecha y hora en que se realizó el depósito */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    /** Descripción opcional del depósito */
    @Column(name = "descripcion")
    private String descripcion;

    /** Estado actual del depósito (ejemplo: "PENDIENTE", "APROBADO", "RECHAZADO") */
    @Column(name = "estado")
    private String estado;

    /**
     * Constructor por defecto.
     * Establece la fecha actual como la fecha del depósito
     * y el estado inicial como "PENDIENTE".
     */
    public Deposito() {
        this.fecha = new Date();
        this.estado = "PENDIENTE";
    }

    // Getters y Setters

    /** @return el ID del depósito */
    public Long getId() { return id; }

    /** @param id el ID del depósito */
    public void setId(Long id) { this.id = id; }

    /** @return el CI del cliente */
    public String getCi() { return ci; }

    /** @param ci el CI del cliente */
    public void setCi(String ci) { this.ci = ci; }

    /** @return el monto del depósito */
    public BigDecimal getMonto() { return monto; }

    /** @param monto el monto a establecer */
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    /** @return la fecha del depósito */
    public Date getFecha() { return fecha; }

    /** @param fecha la fecha a establecer */
    public void setFecha(Date fecha) { this.fecha = fecha; }

    /** @return la descripción del depósito */
    public String getDescripcion() { return descripcion; }

    /** @param descripcion descripción opcional del depósito */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** @return el estado actual del depósito */
    public String getEstado() { return estado; }

    /** @param estado el estado del depósito */
    public void setEstado(String estado) { this.estado = estado; }
}
