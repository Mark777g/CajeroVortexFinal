/*
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 25/7/25
  Time: 37:56
*/

package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Representa un retiro de dinero sin tarjeta, registrado en la base de datos.
 * 
 * Esta entidad se mapea a la tabla `retiros_sin_tarjeta` e incluye información 
 * relevante como el CI del solicitante, el monto retirado, la fecha del retiro,
 * un código único de retiro y el estado del mismo.
 * 
 * El estado por defecto es "PENDIENTE" y la fecha se asigna automáticamente al momento de crear el objeto.
 * 
 * Campos:
 * <ul>
 *     <li>id: Identificador único generado automáticamente</li>
 *     <li>ci: Documento de identidad del cliente</li>
 *     <li>monto: Monto del retiro</li>
 *     <li>fecha: Fecha y hora del retiro</li>
 *     <li>codigoRetiro: Código único para validar el retiro</li>
 *     <li>estado: Estado actual del retiro (ej. "PENDIENTE", "COMPLETADO")</li>
 * </ul>
 * 
 * Reglas de validación:
 * <ul>
 *     <li>ci, monto, fecha y codigoRetiro son obligatorios</li>
 *     <li>codigoRetiro debe ser único</li>
 * </ul>
 * 
 * @author 
 */
@Entity
@Table(name = "retiros_sin_tarjeta")
public class RetiroSinTarjeta implements Serializable {

    /** Identificador único del retiro */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** CI (cédula de identidad) del cliente que realiza el retiro */
    @NotNull
    @Column(name = "ci", nullable = false)
    private String ci;

    /** Monto a retirar */
    @NotNull
    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    /** Fecha y hora en que se registra el retiro */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    /** Código único asociado al retiro sin tarjeta */
    @NotNull
    @Column(name = "codigo_retiro", nullable = false, unique = true)
    private String codigoRetiro;

    /** Estado actual del retiro (ej. PENDIENTE, COMPLETADO) */
    @Column(name = "estado")
    private String estado;

    /**
     * Constructor por defecto.
     * Asigna la fecha actual al campo `fecha` y establece el estado como "PENDIENTE".
     */
    public RetiroSinTarjeta() {
        this.fecha = new Date();
        this.estado = "PENDIENTE";
    }

    // Getters y Setters

    /** @return el ID del retiro */
    public Long getId() { return id; }

    /** @param id el ID a establecer */
    public void setId(Long id) { this.id = id; }

    /** @return el CI del cliente */
    public String getCi() { return ci; }

    /** @param ci el CI del cliente */
    public void setCi(String ci) { this.ci = ci; }

    /** @return el monto del retiro */
    public BigDecimal getMonto() { return monto; }

    /** @param monto el monto a establecer */
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    /** @return la fecha del retiro */
    public Date getFecha() { return fecha; }

    /** @param fecha la fecha a establecer */
    public void setFecha(Date fecha) { this.fecha = fecha; }

    /** @return el código único del retiro */
    public String getCodigoRetiro() { return codigoRetiro; }

    /** @param codigoRetiro el código del retiro a establecer */
    public void setCodigoRetiro(String codigoRetiro) { this.codigoRetiro = codigoRetiro; }

    /** @return el estado actual del retiro */
    public String getEstado() { return estado; }

    /** @param estado el nuevo estado del retiro */
    public void setEstado(String estado) { this.estado = estado; }
}
