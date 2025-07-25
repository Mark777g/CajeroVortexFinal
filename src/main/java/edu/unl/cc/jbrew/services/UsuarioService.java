package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Usuario;
import edu.unl.cc.jbrew.util.EncryptorManager;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;

@Stateless
public class UsuarioService {
    @PersistenceContext
    private EntityManager em;

    public void registrarUsuario(Usuario usuario) {
        em.persist(usuario);
    }

    public Usuario autenticar(String ci, String password) {
        try {
            Usuario usuario = em.createQuery("SELECT u FROM Usuario u WHERE u.ci = :ci", Usuario.class)
                .setParameter("ci", ci)
                .getSingleResult();
            String pwdEncrypted = EncryptorManager.encrypt(password);
            System.out.println("LOGIN DEBUG: ci=" + ci + ", pwdEncrypted=" + pwdEncrypted + ", dbPwd=" + usuario.getPassword());
            if (pwdEncrypted.equals(usuario.getPassword())) {
                return usuario;
            }
            return null;
        } catch (NoResultException e) {
            System.out.println("LOGIN DEBUG: Usuario no encontrado para ci=" + ci);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Usuario buscarPorCi(String ci) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.ci = :ci", Usuario.class)
                     .setParameter("ci", ci)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
} 