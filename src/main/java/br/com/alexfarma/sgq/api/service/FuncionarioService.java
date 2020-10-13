package br.com.alexfarma.sgq.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.alexfarma.sgq.api.model.Permissao;
import br.com.alexfarma.sgq.api.model.Usuario;
import br.com.alexfarma.sgq.api.repository.FuncionarioRepository;
import br.com.alexfarma.sgq.api.repository.UsuarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Usuario criar(Usuario u) {

		if (usuarioRepository.existsById(u.getFuncionarioId())) {
			throw new IllegalArgumentException("Oops! Encontrei um registro no cadastro funcionários com o Código informado.");
		}

		if (u.getUserId() != null && !StringUtils.isEmpty(u.getUserId())) {

			if (userIdExist(u.getUserId())) {
				throw new IllegalArgumentException("Oops! Encontrei um registro no cadastro usuários com o Código, Nome ou E-mail informado!");
			}

			if (u.getSenha() == null || StringUtils.isEmpty(u.getSenha())) {
				throw new IllegalArgumentException("Oops! Você informou um ID de Login, por isso a Senha é obrigatória.");
			}

			u.setSenha(passwordEncoder.encode(u.getSenha()));

		}

		return usuarioRepository.save(u);

	}

	public Usuario atualizar(Long codigo, Usuario u) {

		Usuario usuarioSalvo = buscarUsuarioPeloCodigo(codigo);

		if (u.getUserId() != null && !StringUtils.isEmpty(u.getUserId())) {

			if (!u.getUserId().equalsIgnoreCase(usuarioSalvo.getUserId()) && userIdExist(u.getUserId())) {
				throw new IllegalArgumentException("Oops! Encontrei um registro no cadastro usuários com o Código, Nome ou E-mail informado!");
			}

			if (u.getSenha() != null && !StringUtils.isEmpty(u.getSenha())) {
				u.setSenha(passwordEncoder.encode(u.getSenha()));
			} else if (usuarioSalvo.getSenha() == null) {
				throw new IllegalArgumentException("Oops! Você informou o ID de Login do Usuário, mas a Senha ainda não foi definida.");
			} else {
				u.setSenha(usuarioSalvo.getSenha());
			}

		}

		BeanUtils.copyProperties(u, usuarioSalvo, "codigo", "permissao");

		return usuarioRepository.save(usuarioSalvo);

	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Usuario usuarioSalvo = buscarUsuarioPeloCodigo(codigo);
		usuarioSalvo.setAtivo(ativo);
		funcionarioRepository.save(usuarioSalvo);		
	}

	public void atualizarListaPermissoes(Long codigo, List<Permissao> permissoes) {
		Usuario usuarioSalvo = buscarUsuarioPeloCodigo(codigo);
		usuarioSalvo.setPermissao(permissoes);
		funcionarioRepository.save(usuarioSalvo);
	}

	public Usuario buscarUsuarioPeloCodigo(Long codigo) {
		Usuario usuarioSalvo = usuarioRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return usuarioSalvo;
	}

	private boolean userIdExist(String userId) {
		Optional<Usuario> user = usuarioRepository.findByUserId(userId);
		if (user.isPresent()) {
			return true;
		}
		return false;
	}

}
