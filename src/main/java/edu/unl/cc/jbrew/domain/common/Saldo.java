package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "saldos")
public class Saldo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ci", nullable = false, unique = true)
    private String ci;

    @NotNull
    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;

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