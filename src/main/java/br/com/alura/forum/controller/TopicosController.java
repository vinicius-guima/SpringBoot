package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

//@Controller
@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository; // Dependecy Injection

	@Autowired
	private CursoRepository cursoRepository; // Dependecy Injection

	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {
		// System.out.println(nomeCurso); parametro chega aqui
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		} else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}
	}

	// boa pratica constrir uma URI que informe aonde está o obj criado com
	// UriComponentBuilder

	@PostMapping
	private ResponseEntity<TopicoDto> cadastar(@Valid @RequestBody TopicoForm form, UriComponentsBuilder builder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		URI uri = builder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public DetalhesDoTopicoDto detalhar(@PathVariable("id") Long id) {
		Topico topico = topicoRepository.getOne(id);
		return new DetalhesDoTopicoDto(topico);

	}

	@PutMapping("/{id}")
	private ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Topico topico = form.atualizar(id, topicoRepository);
		topicoRepository.save(topico);
		return ResponseEntity.ok(new TopicoDto(topico));
	}
}
