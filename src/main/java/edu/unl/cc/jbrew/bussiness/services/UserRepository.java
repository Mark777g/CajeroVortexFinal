package edu.unl.cc.jbrew.bussiness.services;

import edu.unl.cc.jbrew.domain.security.User;
import edu.unl.cc.jbrew.exception.EntityNotFoundException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;

import java.util.*;

/**
 *
 * @author wduck
 */
@Stateless
public class UserRepository {
    
    @Inject
    CrudGenericService crudService;

    public UserRepository() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User save(User user){
        if (user.getId() == null){
            return crudService.create(user);
        } else {
            return crudService.update(user);
        }
    }    
    
    public User find(@NotNull Long id) throws EntityNotFoundException {
        User user = crudService.find(User.class, id);
        if (user == null){
            throw new EntityNotFoundException("User no encontrado con [" + id + "]");
        }
        return user;
    }

    public User find(@NotNull String name) throws EntityNotFoundException{
        Map<String, Object> params = new HashMap<>();
        params.put("name", name.toLowerCase());
        User userFound = (User) crudService.findSingleResultOrNullWithNamedQuery("User.findByName", params);
        if (userFound == null){
            throw new EntityNotFoundException("User no encontrado con [" + name + "]");
        }
        return userFound;
    }

    public List<User> findWithLike(@NotNull String name) throws EntityNotFoundException{
        Map<String, Object> params = new HashMap<>();
        params.put("name", name.toLowerCase() + "%");
        return crudService.findWithNamedQuery("User.findLikeName", params);
    }

}
