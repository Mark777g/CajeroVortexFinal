/**
  Created by IntelliJ IDEA.
  User: Mark Gonzalez
  Date: 26/7/25
  Time: 22:24
*/


package edu.unl.cc.jbrew.services;

import edu.unl.cc.jbrew.domain.common.Usuario;
import edu.unl.cc.jbrew.util.EncryptorManager;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con la entidad {@link Usuario}.
 * Permite registrar usuarios, autenticarlos y buscarlos por su cédula de identidad (CI).
 */
@Stateless
public class UsuarioService {

    /**
     * EntityManager inyectado para interactuar con la base de datos mediante JPA.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario el objeto {@link Usuario} que se desea persistir.
     */
    public void registrarUsuario(Usuario usuario) {
        em.persist(usuario);
    }

    /**
     * Autentica a un usuario verificando su cédula y contraseña.
     * La contraseña se compara encriptada con la almacenada en la base de datos.
     *
     * @param ci número de cédula del usuario.
     * @param password contraseña en texto plano que se encriptará para la comparación.
     * @return el objeto {@link Usuario} si las credenciales son correctas; {@code null} si no lo son.
     */
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

    /**
     * Busca un usuario por su cédula de identidad (CI).
     *
     * @param ci número de cédula del usuario.
     * @return el objeto {@link Usuario} si existe; {@code null} si no se encuentra.
     */
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
