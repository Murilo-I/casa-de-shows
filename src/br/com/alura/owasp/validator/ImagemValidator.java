package br.com.alura.owasp.validator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.alura.owasp.model.Usuarios;

@Component
public class ImagemValidator {

	public boolean tratarImagem(MultipartFile imagem, Usuarios usuario, HttpServletRequest request)
			throws IllegalStateException, IOException {
		
		ByteArrayInputStream bytes = new ByteArrayInputStream(imagem.getBytes());
		String mime = URLConnection.guessContentTypeFromStream(bytes);
		
		if(mime==null) {
			return false;
		}
		
		usuario.setNomeImagem(imagem.getOriginalFilename());
		File arquivo = new File(request.getServletContext().getRealPath("/image"), usuario.getNomeImagem());
		imagem.transferTo(arquivo);
		return true;
	}
}
