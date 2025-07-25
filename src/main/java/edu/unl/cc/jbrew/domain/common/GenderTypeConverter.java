package edu.unl.cc.jbrew.domain.common;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "genderTypeConverter", forClass = GenderType.class)
public class GenderTypeConverter implements Converter<GenderType> {
    @Override
    public GenderType getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        return GenderType.valueOf(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, GenderType value) {
        return value != null ? value.name() : "";
    }
} 