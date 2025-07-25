package edu.unl.cc.jbrew.bussiness;

import edu.unl.cc.jbrew.bussiness.services.RoleRepository;
import edu.unl.cc.jbrew.bussiness.services.UserRepository;
import edu.unl.cc.jbrew.domain.security.Role;
import edu.unl.cc.jbrew.domain.security.User;
import edu.unl.cc.jbrew.exception.CredentialInvalidException;
import edu.unl.cc.jbrew.exception.EncryptorException;
import edu.unl.cc.jbrew.util.EncryptorManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import edu.unl.cc.jbrew.exception.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class SecurityFacade implements java.io.Serializable{

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    public User create(User user) throws Exception {
        String pwdEncrypted = EncryptorManager.encrypt(user.getPassword());
        user.setPassword(pwdEncrypted);
        try {
            userRepository.find(user.getName());
        } catch (EntityNotFoundException e){
            User userPersisted = userRepository.save(user);
            return userPersisted;
        }
        throw new Exception("Ya existe un usuario con ese nombre");
    }

    public User update(User user) throws Exception {
        if (user.getId() == null){
            return create(user);
        }
        String pwdEncrypted = EncryptorManager.encrypt(user.getPassword());
        user.setPassword(pwdEncrypted);
        try {
            User userFound = userRepository.find(user.getName());
            if  (!userFound.getId().equals(user.getId())){
                throw new Exception("Ya existe otro usuario con ese nombre");
            }
        } catch (EntityNotFoundException ignored) {
        }
        return userRepository.save(user);
    }

    public User find(Long id) throws EntityNotFoundException {
        return userRepository.find(id);
    }

    public User authenticate(String name, String password) throws CredentialInvalidException, EncryptorException {
        try{
            User userFound = userRepository.find(name);
            String pwdEncrypted = EncryptorManager.encrypt(password);
            System.out.println("LOGIN DEBUG: username=" + name + ", pwdEncrypted=" + pwdEncrypted + ", dbPwd=" + userFound.getPassword());
            if (pwdEncrypted.equals(userFound.getPassword())){
                return userFound;
            }
            throw new CredentialInvalidException("Credenciales inválidas para el usuario: " + name);
        } catch (EntityNotFoundException e){
            throw new CredentialInvalidException("Credenciales inválidas para el usuario: " + name);
        }
    }

    public List<User> findUsers(String criteria) throws EntityNotFoundException {
        return userRepository.findWithLike(criteria);
    }

    public Set<Role> findAllRolesWithPermission()  {
        return roleRepository.findAllWithPermissions();
    }

    public Set<Role> findRolesWithPermission(Long userId) throws EntityNotFoundException {
        User user = userRepository.find(userId);
        Role r = roleRepository.find("ADMIN");
        Set<Role> roles = new LinkedHashSet<Role>();
        roles.add(r);
        return roles;
    }

}
