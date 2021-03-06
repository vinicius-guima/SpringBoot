package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	//@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(sort= "id", direction= Direction.ASC,  page= 0, size= 12) Pageable pageable) {
		
		///topicos?page=0&size=3&sort=id,desc parametros devem vir com assa nomenclatura padrao do Spring
		//(page size sort ,asc ou desc) parametro sort pode aparecer diversas vezes ordenando por 1 ou n campos
		
		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(pageable);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, pageable);
			return TopicoDto.converter(topicos);
		}
	}

	// boa pratica constrir uma URI que informe aonde está o obj criado com
	// UriComponentBuilder
	@PostMapping
	//@CacheEvict(value = "listaDeTopicos" , allEntries = true)  //Cahe quebrando a aplicação
	private ResponseEntity<TopicoDto> cadastar(@Valid @RequestBody TopicoForm form, UriComponentsBuilder builder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		URI uri = builder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			System.out.println(optional.get().getTitulo());
			return ResponseEntity.ok(new DetalhesDoTopicoDto(optional.get()));
		}
		return ResponseEntity.notFound().build();

	}

	// deveria salvar com a anotation @transactional mas não salva
	// por boas praticas adiciono a todos os metodos que interferem np bd
	@PutMapping("/{id}")
	@Transactional
	//@CacheEvict(value = "listaDeTopicos" , allEntries = true)
	private ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			topicoRepository.save(topico);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	//@CacheEvict(value = "listaDeTopicos" , allEntries = true)
	private ResponseEntity<?> remover(@PathVariable(value = ("id"))Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();			
		}
		return ResponseEntity.notFound().build();
	}
}
