package br.com.alexfarma.sgq.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alexfarma.sgq.api.model.Usuario;
import br.com.alexfarma.sgq.api.repository.UsuarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOpt = usuarioRepository.findByUserIdAndAtivoTrue(userId);
		Usuario usuario = usuarioOpt.orElseThrow(() -> new UsernameNotFoundException("Usuário inválido e/ou senha incorreta!"));
		return new UsuarioSistema(usuario, getPermissoes(usuario));
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		usuario.getPermissao().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getRoleName().toUpperCase())));
		return authorities;
	}

}
