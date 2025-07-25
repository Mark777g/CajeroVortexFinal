package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotEmpty
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @NotNull @NotEmpty
    @Column(name = "genero", nullable = false)
    private String genero;

    @NotNull @Past
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fechaNacimiento;

    @NotNull @NotEmpty
    @Column(name = "ci", unique = true, nullable = false)
    @Size(min = 6, message = "El CI debe tener al menos 6 caracteres")
    private String ci;

    @NotNull @NotEmpty
    @Column(name = "password", nullable = false)
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "terminos_aceptados", nullable = false)
    private boolean terminosAceptados;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro", updatable = false)
    private Date fechaRegistro;

    public Usuario() {
        this.fechaRegistro = new Date();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isTerminosAceptados() { return terminosAceptados; }
    public void setTerminosAceptados(boolean terminosAceptados) { this.terminosAceptados = terminosAceptados; }

    public Date getFechaRegistro() { return fechaRegistro; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(ci, usuario.ci);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ci);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", ci='" + ci + '\'' +
                '}';
    }
}