/**
  Created by IntelliJ IDEA.
  User: Joseph Aguilar
  Date: 25/7/25
  Time: 14:10
*/

package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Representa una transferencia bancaria entre dos cuentas.
 * 
 * Esta entidad se mapea a la tabla `transferencias` y almacena información relacionada
 * con el movimiento de fondos entre una cuenta de origen y una cuenta de destino, 
 * incluyendo datos como el CI del titular, monto, fecha, estado y una descripción opcional.
 * 
 * Al instanciarse, la transferencia se marca automáticamente con la fecha actual
 * y un estado inicial de "PENDIENTE".
 * 
 * <b>Campos clave:</b>
 * <ul>
 *   <li><b>id</b>: Identificador único de la transferencia.</li>
 *   <li><b>cuentaOrigen</b>: Número de cuenta desde donde se transfiere el dinero.</li>
 *   <li><b>cuentaDestino</b>: Número de cuenta que recibe el dinero.</li>
 *   <li><b>monto</b>: Cantidad de dinero transferida.</li>
 *   <li><b>fecha</b>: Fecha y hora de la operación.</li>
 *   <li><b>ci</b>: Cédula de identidad del cliente que realiza la transferencia.</li>
 *   <li><b>descripcion</b>: Detalle opcional de la transferencia.</li>
 *   <li><b>estado</b>: Estado actual de la transferencia (ej. "PENDIENTE", "COMPLETADA").</li>
 * </ul>
 * 
 * <b>Validaciones:</b>
 * <ul>
 *   <li>Los campos <code>cuentaOrigen</code>, <code>cuentaDestino</code>, <code>monto</code>, 
 *       <code>fecha</code> y <code>ci</code> son obligatorios.</li>
 * </ul>
 * 
 * Esta clase implementa {@link java.io.Serializable} para permitir su persistencia y transmisión.
 * 
 * @author 
 */
@Entity
@Table(name = "transferencias")
public class Transferencia implements Serializable {

    /** Identificador único de la transferencia */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Número de cuenta desde la cual se realiza la transferencia */
    @NotNull
    @Column(name = "cuenta_origen", nullable = false)
    private String cuentaOrigen;

    /** Número de cuenta que recibe el dinero */
    @NotNull
    @Column(name = "cuenta_destino", nullable = false)
    private String cuentaDestino;

    /** Monto transferido entre cuentas */
    @NotNull
    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    /** Fecha y hora en que se registra la transferencia */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    /** Cédula de identidad del cliente que realiza la transferencia */
    @NotNull
    @Column(name = "ci", nullable = false)
    private String ci;

    /** Descripción opcional del motivo o concepto de la transferencia */
    @Column(name = "descripcion")
    private String descripcion;

    /** Estado actual de la transferencia (por defecto "PENDIENTE") */
    @Column(name = "estado")
    private String estado;

    /**
     * Constructor por defecto.
     * Asigna la fecha actual como momento de la transferencia
     * y establece el estado inicial como "PENDIENTE".
     */
    public Transferencia() {
        this.fecha = new Date();
        this.estado = "PENDIENTE";
    }

    // Getters y Setters

    /** @return el ID de la transferencia */
    public Long getId() { return id; }

    /** @param id el identificador a establecer */
    public void setId(Long id) { this.id = id; }

    /** @return la cuenta de origen */
    public String getCuentaOrigen() { return cuentaOrigen; }

    /** @param cuentaOrigen la cuenta de origen a establecer */
    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }

    /** @return la cuenta de destino */
    public String getCuentaDestino() { return cuentaDestino; }

    /** @param cuentaDestino la cuenta de destino a establecer */
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }

    /** @return el monto de la transferencia */
    public BigDecimal getMonto() { return monto; }

    /** @param monto el monto a establecer */
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    /** @return la fecha de la transferencia */
    public Date getFecha() { return fecha; }

    /** @param fecha la fecha a establecer */
    public void setFecha(Date fecha) { this.fecha = fecha; }

    /** @return la descripción de la transferencia */
    public String getDescripcion() { return descripcion; }

    /** @param descripcion la descripción a establecer */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** @return el estado actual de la transferencia */
    public String getEstado() { return estado; }

    /** @param estado el estado a establecer */
    public void setEstado(String estado) { this.estado = estado; }

    /** @return el CI del cliente que realiza la transferencia */
    public String getCi() { return ci; }

    /** @param ci el CI a establecer */
    public void setCi(String ci) { this.ci = ci; }
}
