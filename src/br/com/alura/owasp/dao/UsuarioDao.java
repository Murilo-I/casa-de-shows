package br.com.alura.owasp.dao;

import br.com.alura.owasp.model.Usuarios;

public interface UsuarioDao {
	public void salva(Usuarios usuario);
	public Usuarios procuraUsuario(Usuarios usuario);
	public void setRole(Usuarios usuarioRegistro, String role);
}
