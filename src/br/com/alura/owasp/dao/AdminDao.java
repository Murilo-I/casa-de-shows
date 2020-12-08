package br.com.alura.owasp.dao;

import br.com.alura.owasp.model.Usuarios;

public interface AdminDao {
	public boolean verificaSeUsuarioEhAdmin(Usuarios usuario);
}
