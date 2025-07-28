/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 25/7/25
  Time: 15:05
*/
package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Representa un retiro de dinero utilizando una tarjeta en el sistema.
 * 
 * Esta entidad se mapea a la tabla `retiros_tarjeta` en la base de datos
 * y almacena la información asociada al retiro como el CI del cliente, 
 * el número de tarjeta, el monto retirado, la fecha y un número de transacción único.
 * 
 * Reglas de validación:
 * <ul>
 *   <li>Todos los campos son obligatorios.</li>
 *   <li>El campo {@code numeroTransaccion} debe ser único para evitar duplicidades.</li>
 * </ul>
 * 
 * Esta clase implementa {@link java.io.Serializable} para permitir su persistencia.
 * 
 * Campos:
 * <ul>
 *   <li><b>id</b>: Identificador único del retiro (autogenerado).</li>
 *   <li><b>ci</b>: Cédula de identidad del cliente.</li>
 *   <li><b>numeroTarjeta</b>: Número de tarjeta utilizada para el retiro.</li>
 *   <li><b>monto</b>: Monto retirado.</li>
 *   <li><b>fecha</b>: Fecha y hora del retiro.</li>
 *   <li><b>numeroTransaccion</b>: Código único de la transacción.</li>
 * </ul>
 * 
 * @author 
 */
@Entity
@Table(name = "retiros_tarjeta")
public class RetiroTarjeta implements Serializable {

    /** Identificador único del retiro (clave primaria) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cédula de identidad del cliente que realiza el retiro */
    @Column(name = "ci", nullable = false)
    private String ci;

    /** Número de tarjeta utilizada en el retiro */
    @Column(name = "numero_tarjeta", nullable = false)
    private String numeroTarjeta;

    /** Monto del retiro */
    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    /** Fecha y hora del retiro */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    /** Número de transacción único asociado al retiro */
    @Column(name = "numero_transaccion", nullable = false, unique = true)
    private String numeroTransaccion;

    /**
     * Constructor por defecto.
     * Se requiere establecer manualmente todos los campos antes de persistir la entidad.
     */
    public RetiroTarjeta() {}

    // Getters y Setters

    /** @return el ID del retiro */
    public Long getId() { return id; }

    /** @param id el ID a establecer */
    public void setId(Long id) { this.id = id; }

    /** @return el CI del cliente */
    public String getCi() { return ci; }

    /** @param ci el CI del cliente */
    public void setCi(String ci) { this.ci = ci; }

    /** @return el número de tarjeta utilizada */
    public String getNumeroTarjeta() { return numeroTarjeta; }

    /** @param numeroTarjeta el número de tarjeta a establecer */
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    /** @return el monto retirado */
    public BigDecimal getMonto() { return monto; }

    /** @param monto el monto del retiro */
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    /** @return la fecha y hora del retiro */
    public Date getFecha() { return fecha; }

    /** @param fecha la fecha del retiro */
    public void setFecha(Date fecha) { this.fecha = fecha; }

    /** @return el número de transacción único */
    public String getNumeroTransaccion() { return numeroTransaccion; }

    /** @param numeroTransaccion el código único de transacción */
    public void setNumeroTransaccion(String numeroTransaccion) { this.numeroTransaccion = numeroTransaccion; }
}
