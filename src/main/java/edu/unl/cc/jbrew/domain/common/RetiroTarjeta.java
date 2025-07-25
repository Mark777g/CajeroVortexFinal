package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "retiros_tarjeta")
public class RetiroTarjeta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ci", nullable = false)
    private String ci;

    @Column(name = "numero_tarjeta", nullable = false)
    private String numeroTarjeta;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @Column(name = "numero_transaccion", nullable = false, unique = true)
    private String numeroTransaccion;

    public RetiroTarjeta() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getNumeroTransaccion() { return numeroTransaccion; }
    public void setNumeroTransaccion(String numeroTransaccion) { this.numeroTransaccion = numeroTransaccion; }
} 