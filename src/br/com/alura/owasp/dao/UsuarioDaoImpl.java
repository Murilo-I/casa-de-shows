package br.com.alura.owasp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import br.com.alura.owasp.model.Usuarios;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Usuarios usuario) {
		encriptarSenha(usuario);
		manager.persist(usuario);
	}

	private void encriptarSenha(Usuarios usuario) {
		String salt = BCrypt.gensalt();
		String hashpw = BCrypt.hashpw(usuario.getSenha(), salt);
		usuario.setSenha(hashpw);
	}

	public Usuarios procuraUsuario(Usuarios usuario) {
		Usuarios banco = manager.createQuery("select u from Usuarios u where u.email = :email", Usuarios.class)
				.setParameter("email", usuario.getEmail()).getResultList().stream().findFirst().orElse(null);

		if (validarSenha(usuario, banco)) {
			return banco;
		}

		return null;
	}

	private boolean validarSenha(Usuarios usuarioWeb, Usuarios usuarioBanco) {
		if (usuarioBanco == null) {
			return false;
		} else {
			return BCrypt.checkpw(usuarioWeb.getSenha(), usuarioBanco.getSenha());
		}
	}

	public void setRole(Usuarios usuario, String role) {
//		manager.createNativeQuery(
//				"insert into usuarios_roles(Usuarios_email, roles_name) values (?, ?);")
//				.setParameter(1, usuario.getEmail()).setParameter(2, role).executeUpdate();
	}
}
