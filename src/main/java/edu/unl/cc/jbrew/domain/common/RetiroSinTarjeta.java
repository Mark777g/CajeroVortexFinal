package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "retiros_sin_tarjeta")
public class RetiroSinTarjeta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ci", nullable = false)
    private String ci;

    @NotNull
    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @NotNull
    @Column(name = "codigo_retiro", nullable = false, unique = true)
    private String codigoRetiro;

    @Column(name = "estado")
    private String estado;

    public RetiroSinTarjeta() {
        this.fecha = new Date();
        this.estado = "PENDIENTE";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getCodigoRetiro() { return codigoRetiro; }
    public void setCodigoRetiro(String codigoRetiro) { this.codigoRetiro = codigoRetiro; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
} 