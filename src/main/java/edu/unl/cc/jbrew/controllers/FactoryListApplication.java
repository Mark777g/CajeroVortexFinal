package edu.unl.cc.jbrew.controllers;

import edu.unl.cc.jbrew.domain.common.GenderType;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

@Named("factoryListApp")
@ApplicationScoped
public class FactoryListApplication implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    private List<GenderType> genderOptions;

    @PostConstruct
    public void init() {
        genderOptions = Arrays.asList(GenderType.values());
    }

    public List<GenderType> getGenderOptions() {
        return genderOptions;
    }
}
