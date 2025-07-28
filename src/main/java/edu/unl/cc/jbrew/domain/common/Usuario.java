/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 25/7/25
  Time: 16:01
*/
package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Entidad que representa un usuario en el sistema.
 * Esta clase se mapea a la tabla "usuarios" en la base de datos.
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador único del usuario (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombres completos del usuario, no pueden estar vacíos ni nulos. */
    @NotNull
    @NotEmpty
    @Column(name = "nombres", nullable = false)
    private String nombres;

    /** Género del usuario, no puede estar vacío ni nulo. */
    @NotNull
    @NotEmpty
    @Column(name = "genero", nullable = false)
    private String genero;

    /** Fecha de nacimiento del usuario, debe ser una fecha pasada y no nula. */
    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fechaNacimiento;

    /**
     * Cédula de identidad (CI) del usuario, única y obligatoria,
     * con un mínimo de 6 caracteres.
     */
    @NotNull
    @NotEmpty
    @Size(min = 6, message = "El CI debe tener al menos 6 caracteres")
    @Column(name = "ci", unique = true, nullable = false)
    private String ci;

    /**
     * Contraseña del usuario, obligatoria,
     * con un mínimo de 6 caracteres.
     */
    @NotNull
    @NotEmpty
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(name = "password", nullable = false)
    private String password;

    /** Email del usuario, único y obligatorio. */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /** Indica si el usuario ha aceptado los términos y condiciones. */
    @Column(name = "terminos_aceptados", nullable = false)
    private boolean terminosAceptados;

    /** Fecha y hora en que se registró el usuario. No se actualiza después de creado. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro", updatable = false)
    private Date fechaRegistro;

    /**
     * Constructor por defecto que inicializa la fecha de registro con la fecha actual.
     */
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

    /**
     * Dos usuarios son iguales si tienen la misma cédula de identidad (CI).
     * @param o objeto a comparar
     * @return true si las CIs son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Usuario usuario = (Usuario) o;
        return Objects.equals(ci, usuario.ci);
    }

    /**
     * Calcula el código hash usando la cédula de identidad (CI).
     * @return código hash basado en la CI
     */
    @Override
    public int hashCode() {
        return Objects.hash(ci);
    }

    /**
     * Representación en cadena del usuario mostrando id, nombres y CI.
     * @return String con información básica del usuario
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", ci='" + ci + '\'' +
                '}';
    }
}
