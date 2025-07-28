
/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 24/7/25
  Time: 20:32
*/
package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.domain.common.GenderType;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

/**
 * Bean de aplicación que provee listas estáticas para la aplicación.
 * <p>
 * Actualmente proporciona la lista de opciones para el tipo de género, 
 * cargándolas al iniciar la aplicación.
 * </p>
 */
@Named("factoryListApp")
@ApplicationScoped
public class FactoryListApplication implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Lista de opciones de género disponibles.
     */
    private List<GenderType> genderOptions;

    /**
     * Inicializa las listas estáticas después de la construcción del bean.
     * Carga todas las constantes definidas en {@link GenderType}.
     */
    @PostConstruct
    public void init() {
        genderOptions = Arrays.asList(GenderType.values());
    }

    /**
     * Obtiene la lista de opciones de género para su uso en la aplicación.
     * 
     * @return lista de {@link GenderType}
     */
    public List<GenderType> getGenderOptions() {
        return genderOptions;
    }
}
