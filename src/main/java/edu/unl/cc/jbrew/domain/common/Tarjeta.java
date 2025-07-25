package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tarjetas")
public class Tarjeta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ci", nullable = false)
    private String ci;

    @NotNull
    @Column(name = "numero", nullable = false, unique = true)
    private String numero;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_expiracion", nullable = false)
    private Date fechaExpiracion;

    @NotNull
    @Column(name = "nombre_titular", nullable = false)
    private String nombreTitular;

    @NotNull
    @Column(name = "cvc", nullable = false, length = 3)
    private String cvc;

    @Column(name = "estado")
    private String estado;

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