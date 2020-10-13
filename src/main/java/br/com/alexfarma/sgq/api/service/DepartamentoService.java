package br.com.alexfarma.sgq.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.alexfarma.sgq.api.model.Departamento;
import br.com.alexfarma.sgq.api.repository.DepartamentoRepository;

@Service
public class DepartamentoService {

	@Autowired
	private DepartamentoRepository departamentoRepository;

	public Departamento atualizar(Long codigo, Departamento d) {
		Departamento dptoSalvo = buscarDepartamentoPeloCodigo(codigo);
		BeanUtils.copyProperties(d, dptoSalvo, "codigo");
		return departamentoRepository.save(dptoSalvo);
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Departamento dptoSalvo = buscarDepartamentoPeloCodigo(codigo);
		dptoSalvo.setAtivo(ativo);
		departamentoRepository.save(dptoSalvo);		
	}

	public Departamento buscarDepartamentoPeloCodigo(Long codigo) {
		Departamento dptoSalvo = departamentoRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return dptoSalvo;
	}

}
