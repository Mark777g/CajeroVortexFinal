/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 10/7/25
  Time: 10:30
*/

package edu.unl.cc.jbrew.domain.common;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

/**
 * Convertidor JSF para la enumeración {@link GenderType}.
 * 
 * Este convertidor permite la conversión automática entre cadenas (`String`)
 * y valores del enum `GenderType` al usar componentes JSF como {@code <h:selectOneMenu>}.
 * 
 * Se registra con el valor "genderTypeConverter" y se asocia directamente con la clase `GenderType`
 * mediante el atributo `forClass`.
 * 
 * Ejemplo de uso en una vista JSF:
 * <pre>{@code
 * <h:selectOneMenu value="#{bean.genero}" converter="genderTypeConverter">
 *     <f:selectItem itemValue="MALE" itemLabel="Masculino"/>
 *     <f:selectItem itemValue="FEMALE" itemLabel="Femenino"/>
 * </h:selectOneMenu>
 * }</pre>
 * 
 * Implementa la interfaz {@link Converter}, que define dos métodos:
 * <ul>
 *   <li>{@code getAsObject} — convierte un {@code String} en un valor del enum {@code GenderType}</li>
 *   <li>{@code getAsString} — convierte un {@code GenderType} en su representación como {@code String}</li>
 * </ul>
 * 
 * @author 
 */
@FacesConverter(value = "genderTypeConverter", forClass = GenderType.class)
public class GenderTypeConverter implements Converter<GenderType> {

    /**
     * Convierte un valor de tipo {@code String} proveniente de la vista
     * en un objeto {@code GenderType}.
     *
     * @param context   el contexto de Faces actual
     * @param component el componente de interfaz de usuario actual
     * @param value     el valor como cadena (por ejemplo, "MALE", "FEMALE")
     * @return el valor correspondiente de {@code GenderType}, o {@code null} si el valor es vacío
     */
    @Override
    public GenderType getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        return GenderType.valueOf(value);
    }

    /**
     * Convierte un objeto {@code GenderType} en su representación como {@code String}.
     *
     * @param context   el contexto de Faces actual
     * @param component el componente de interfaz de usuario actual
     * @param value     el valor del enum a convertir
     * @return el nombre del enum como cadena, o cadena vacía si el valor es {@code null}
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, GenderType value) {
        return value != null ? value.name() : "";
    }
}
