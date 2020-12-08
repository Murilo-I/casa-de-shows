package br.com.alura.owasp.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.alura.owasp.model.Depoimento;

public class DepoimentoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Depoimento.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Depoimento d = (Depoimento) target;
		String titulo = d.getTitulo();
		String msg = d.getMensagem();
		
		if(titulo.contains("<") || titulo.contains(">") || msg.contains("<") || msg.contains(">")) {
			errors.rejectValue("titulo", "errors.titulo");
			errors.rejectValue("mensagem", "errors.mensagem");
		}
	}	
}
