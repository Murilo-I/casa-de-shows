package br.com.alura.owasp.dao;

import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import br.com.alura.owasp.model.Roles;
import br.com.alura.owasp.model.Usuarios;

@Repository
public class AdminDaoImpl implements AdminDao {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public boolean verificaSeUsuarioEhAdmin(Usuarios usuario) {
		TypedQuery<Usuarios> query = manager.createQuery(
				"select u from Usuarios u where u.email =:email", Usuarios.class);
		query.setParameter("email", usuario.getEmail());
		Usuarios usuarioRetornado = query.getResultList().stream().findFirst()
				.orElse(null);

		if (validaSenhaDoUsuarioComOHAshDoBanco(usuario, usuarioRetornado)) {
			return verificaSeUsuarioTemRoleAdmin(usuarioRetornado);
		}

		return false;
	}
	
	private boolean validaSenhaDoUsuarioComOHAshDoBanco(Usuarios usuario,
			Usuarios usuarioRetornado) {
		if (usuarioRetornado == null) {
			return false;
		}
		boolean comparaSenhaComHashESemHash = BCrypt.checkpw(
				usuario.getSenha(), usuarioRetornado.getSenha());
		return comparaSenhaComHashESemHash;
	}

	private boolean verificaSeUsuarioTemRoleAdmin(Usuarios usuarioRetornado) {
		return usuarioRetornado.getRoles().stream().map(Roles::getName)
				.collect(Collectors.toList()).contains("ROLE_ADMIN");

	}
}
