package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.domain.common.GenderType;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.io.Serial;
import java.util.Arrays;
import java.util.List;

/**
 * Clase factory que provee listas estáticas para ser utilizadas en toda la aplicación.
 * 
 * <p>Alcance: ApplicationScoped (una única instancia para toda la aplicación)</p>
 * 
 * <p>Propósito principal: Proporcionar listas predefinidas de opciones para componentes
 * de UI que necesitan valores constantes como tipos de género.</p>
 */
@Named("factoryListApp")
@ApplicationScoped
public class FactoryListApplication implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Lista de opciones de género disponible a nivel de aplicación
    private List<GenderType> genderOptions;

    /**
     * Método de inicialización que se ejecuta después de la construcción del bean.
     * Carga todas las listas estáticas que serán utilizadas en la aplicación.
     */
    @PostConstruct
    public void init() {
        // Carga todas las opciones de género desde el enum GenderType
        genderOptions = Arrays.asList(GenderType.values());
    }

    /**
     * Obtiene la lista de opciones de género disponibles.
     * 
     * @return List<GenderType> lista inmutable de los valores del enum GenderType
     */
    public List<GenderType> getGenderOptions() {
        return genderOptions;
    }
}
