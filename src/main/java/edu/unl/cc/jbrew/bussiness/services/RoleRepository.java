package edu.unl.cc.jbrew.bussiness.services;

import edu.unl.cc.jbrew.domain.security.Permission;
import edu.unl.cc.jbrew.domain.security.Role;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Stateless
public class RoleRepository {

    @Inject
    private CrudGenericService crudService;

    public Set<Role> findAllWithPermissions(){
        return new HashSet<>(crudService.findWithQuery("select * from role"));
    }

    public Role find(String name){
        String sql = "SELECT * FROM ROLE WHERE name LIKE ?";
        Query query = crudService.createNativeQuery(sql, Role.class);
        query.setParameter(1, name.toLowerCase());
        Role entity = (Role) crudService.findSingleResultOrNullWithQuery(query);
        if (entity != null) {
            return entity;
        }
        throw new EntityNotFoundException("Role not found to name [" + name + "]");
    }
}
