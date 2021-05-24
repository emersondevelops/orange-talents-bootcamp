package br.com.zup.orangetalents.controller;

import br.com.zup.orangetalents.dto.AvaliacaoDto;
import br.com.zup.orangetalents.model.Avaliacao;
import br.com.zup.orangetalents.repository.AvaliacaoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoController(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @PostMapping
    @CacheEvict(value = "listaDeAvaliacoes", allEntries = true) // Invalida cache em caso de novo cadastro.
    public ResponseEntity<?> cadastrar(@RequestBody @Valid AvaliacaoDto avaliacaoDto,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Avaliacao avaliacao = new Avaliacao(avaliacaoDto.getTitulo(), avaliacaoDto.getDescricao());
        avaliacaoRepository.save(avaliacao);
        URI uri = uriComponentsBuilder.path("/avaliacoes/{id}").buildAndExpand(avaliacao.getId()).toUri();
        return ResponseEntity.created(uri).body(new AvaliacaoDto(avaliacao));
    }

    @GetMapping
    @Cacheable(value = "listaDeAvaliacoes") // Habilita cache para este método.
    public Page<AvaliacaoDto> listar(@RequestParam(required = false) String titulo,
                                     @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if (titulo == null || titulo.isEmpty()) {
            Page<Avaliacao> avaliacoes = avaliacaoRepository.findAll(pageable);
            return AvaliacaoDto.converter(avaliacoes);
        } else {
            Page<Avaliacao> avaliacoes = avaliacaoRepository.findByTitulo(titulo, pageable);
            return AvaliacaoDto.converter(avaliacoes);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDto> detalhar(@PathVariable Long id) {
        Optional<Avaliacao> avaliacao = avaliacaoRepository.findById(id);
        return avaliacao.map(value -> ResponseEntity.ok(new AvaliacaoDto(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaDeAvaliacoes", allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Avaliacao> avaliacao = avaliacaoRepository.findById(id);
        if (avaliacao.isPresent()) {
            avaliacaoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return  ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "listaDeAvaliacoes", allEntries = true)
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid AvaliacaoDto avaliacaoDto) {
        Optional<Avaliacao> avaliacao = avaliacaoRepository.findById(id);
        if (avaliacao.isPresent()) {
            avaliacao.get().setTitulo(avaliacaoDto.getTitulo());
            avaliacao.get().setDescricao(avaliacaoDto.getDescricao());
            return ResponseEntity.ok().body(new AvaliacaoDto(avaliacaoRepository.save(avaliacao.get())));
        }
        return  ResponseEntity.notFound().build();
    }
}
