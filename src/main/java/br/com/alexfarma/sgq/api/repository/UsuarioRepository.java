package br.com.alexfarma.sgq.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alexfarma.sgq.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	public List<Usuario> listarAtivos();

	public Optional<Usuario> findByUserId(String userId);
	public Optional<Usuario> findByUserIdAndAtivoTrue(String userId);

}
