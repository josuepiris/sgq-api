package br.com.alexfarma.sgq.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alexfarma.sgq.api.model.Usuario;
import br.com.alexfarma.sgq.api.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}

	@GetMapping(params = "ativos")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<Usuario> listarAtivos() {
		return usuarioRepository.listarAtivos();
	}

}
