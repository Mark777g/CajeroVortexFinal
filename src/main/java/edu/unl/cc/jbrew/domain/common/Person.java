/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 25/7/25
  Time: 16:24
*/

package edu.unl.cc.jbrew.domain.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad JPA que representa a una persona con información básica
 * como nombre, apellido, fecha de nacimiento, email y género.
 * 
 * Se utiliza para persistencia en base de datos y está marcada con {@code @Entity}.
 * 
 * Valida campos obligatorios y transforma nombres a mayúsculas al establecerlos.
 */
@Entity
public class Person implements Serializable {

    /** Identificador único de la persona (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la persona. No puede ser nulo ni vacío. */
    @NotNull @NotEmpty
    private String firstName;

    /** Apellido de la persona. No puede ser nulo ni vacío. */
    @NotNull @NotEmpty
    private String lastName;

    /** Fecha de nacimiento. No puede ser nula. */
    @NotNull
    private LocalDate birthDate;

    /** Correo electrónico válido. No puede ser nulo ni vacío. */
    @NotNull @NotEmpty
    @Email(message = "Formato de email incorrecto")
    private String email;

    /** Género de la persona representado como enumeración. */
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    /**
     * Constructor por defecto. Asigna el género como MALE y la fecha de nacimiento como la actual.
     */
    public Person() {
        gender = GenderType.MALE;
        birthDate = LocalDate.now();
    }

    /**
     * Constructor con parámetros para inicializar campos clave.
     * 
     * @param id Identificador
     * @param firstName Nombre
     * @param lastName Apellido
     * @param email Correo electrónico
     * @param gender Género
     * @throws IllegalArgumentException si algún campo obligatorio está vacío
     */
    public Person(Long id, String firstName, String lastName, String email, GenderType gender)
            throws IllegalArgumentException {
        this();
        //validateObligatoryField(firstName);
        //validateObligatoryField(lastName);
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.email = email;
        this.gender = gender;
    }

    /**
     * Valida que un campo de texto obligatorio no sea nulo ni vacío.
     * 
     * @param text texto a validar
     * @throws IllegalArgumentException si el texto es nulo o vacío
     */
    private void validateObligatoryField(String text) throws IllegalArgumentException {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Campo obligatorio vacio");
        }
    }

    /**
     * Devuelve el nombre completo de la persona en formato "apellido nombre".
     * 
     * @return Nombre completo
     */
    public String getFullName() {
        return lastName + " " + firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    /**
     * Establece el nombre en mayúsculas.
     * 
     * @param firstName Nombre a establecer
     */
    public final void setFirstName(String firstName) {
        this.firstName = firstName.toUpperCase();
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Establece el apellido en mayúsculas.
     * 
     * @param lastName Apellido a establecer
     */
    public final void setLastName(String lastName) {
        this.lastName = lastName.toUpperCase();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    /**
     * Genera el hashcode de la persona en base a sus atributos.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.firstName);
        hash = 53 * hash + Objects.hashCode(this.lastName);
        hash = 53 * hash + Objects.hashCode(this.birthDate);
        hash = 53 * hash + Objects.hashCode(this.email);
        return hash;
    }

    /**
     * Compara dos objetos {@code Person} por sus campos principales.
     * 
     * @param obj Objeto a comparar
     * @return {@code true} si todos los campos relevantes coinciden
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.birthDate, other.birthDate);
    }

    /**
     * Devuelve una representación de la persona como cadena de texto.
     * 
     * @return Cadena con los datos básicos
     */
    @Override
    public String toString() {
        return "Person{" + "id:" + id + ", firstName:" + firstName + ", lastName:" + lastName + ", birthDate:" + birthDate + ", email:" + email + '}';
    }
}
