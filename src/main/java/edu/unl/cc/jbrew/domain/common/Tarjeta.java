/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 25/7/25
  Time: 16:28
*/
package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Entidad que representa una tarjeta asociada a un usuario identificado por su CI (cédula de identidad).
 * Esta clase se mapea a la tabla "tarjetas" en la base de datos.
 */
@Entity
@Table(name = "tarjetas")
public class Tarjeta implements Serializable {

    /** Identificador único de la tarjeta (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cédula de identidad del titular de la tarjeta, no puede ser nula. */
    @NotNull
    @Column(name = "ci", nullable = false)
    private String ci;

    /** Número único de la tarjeta, no puede ser nulo ni repetido. */
    @NotNull
    @Column(name = "numero", nullable = false, unique = true)
    private String numero;

    /** Tipo de la tarjeta (por ejemplo, crédito o débito), no puede ser nulo. */
    @NotNull
    @Column(name = "tipo", nullable = false)
    private String tipo;

    /** Fecha de expiración de la tarjeta, no puede ser nula. */
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_expiracion", nullable = false)
    private Date fechaExpiracion;

    /** Nombre del titular de la tarjeta, no puede ser nulo. */
    @NotNull
    @Column(name = "nombre_titular", nullable = false)
    private String nombreTitular;

    /** Código CVC de la tarjeta, no puede ser nulo y tiene longitud fija de 3 caracteres. */
    @NotNull
    @Column(name = "cvc", nullable = false, length = 3)
    private String cvc;

    /** Estado actual de la tarjeta, puede ser null. */
    @Column(name = "estado")
    private String estado;

    /**
     * Constructor por defecto que inicializa el estado de la tarjeta a "ACTIVA".
     */
    public Tarjeta() {
        this.estado = "ACTIVA";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Date getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Date fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }
    public String getCvc() { return cvc; }
    public void setCvc(String cvc) { this.cvc = cvc; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
