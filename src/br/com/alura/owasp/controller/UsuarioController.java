package br.com.alura.owasp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.alura.owasp.dao.UsuarioDao;
import br.com.alura.owasp.model.Usuarios;
import br.com.alura.owasp.retrofit.GoogleWebClient;
import br.com.alura.owasp.validator.ImagemValidator;

@Controller
@Transactional
public class UsuarioController {

	@Autowired
	private UsuarioDao dao;
	@Autowired
	private GoogleWebClient client;
	@Autowired ImagemValidator imagemValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//com isso não é mais preciso um dto de user para impedir mass assignment
		binder.setAllowedFields("email", "senha", "nome", "nomeImagem");
	}
	
	@RequestMapping("/usuario")
	public String usuario(Model model) {
		Usuarios usuario = new Usuarios();
		model.addAttribute("usuario", usuario);
		return "usuario";
	}

	@RequestMapping("/usuarioLogado")
	public String usuarioLogado() {
		return "usuarioLogado";
	}

	@RequestMapping(value = "/registrar", method = RequestMethod.POST)
	public String registrar(MultipartFile imagem, @ModelAttribute("usuarioRegistro") Usuarios usuarioRegistro,
			RedirectAttributes redirect, HttpServletRequest request, Model model, HttpSession session)
			throws IllegalStateException, IOException {

		//Usuarios usuarioRegistro = usuarioDTO.parseUsruario();
		boolean ehImagem = imagemValidator.tratarImagem(imagem, usuarioRegistro, request);
		
		if(ehImagem) {
			dao.salva(usuarioRegistro);
			dao.setRole(usuarioRegistro, "ROLE_USER");
			session.setAttribute("usuario", usuarioRegistro);
			model.addAttribute("usuario", usuarioRegistro);
			return "usuarioLogado";			
		}
		
		redirect.addFlashAttribute("mensagem", "O arquivo especificado não é uma imagem");
		return "redirect:/usuario";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("usuario") Usuarios usuario, RedirectAttributes redirect, Model model,
			HttpSession session, HttpServletRequest request) throws IOException {
		
		String recaptcha = request.getParameter("g-recaptcha-response");
		boolean verifyRecaptcha = client.verify(recaptcha);

		if(verifyRecaptcha) {
			return procuraUsuario(usuario, redirect, model, session);
		} else {
			redirect.addFlashAttribute("mensagem", "Favor comprovar se é humano");
			return "redirect:/usuario";
		}
	}

	private String procuraUsuario(Usuarios usuario, RedirectAttributes redirect, Model model, HttpSession session) {
		Usuarios usuarioRetornado = dao.procuraUsuario(usuario);
		model.addAttribute("usuario", usuarioRetornado);
		if (usuarioRetornado == null) {
			redirect.addFlashAttribute("mensagem", "Usuário não encontrado");
			return "redirect:/usuario";
		}

		session.setAttribute("usuario", usuarioRetornado);
		return "usuarioLogado";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("usuario");
		return "usuario";
	}
}
