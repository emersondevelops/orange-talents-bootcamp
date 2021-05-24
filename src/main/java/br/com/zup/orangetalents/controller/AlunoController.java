package br.com.zup.orangetalents.controller;

import br.com.zup.orangetalents.dto.AlunoDetalhesDto;
import br.com.zup.orangetalents.dto.AlunoDto;
import br.com.zup.orangetalents.model.Aluno;
import br.com.zup.orangetalents.repository.AlunoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @PostMapping
    @CacheEvict(value = "listaDeAlunos", allEntries = true) // Invalida cache em caso de novo cadastro.
    public ResponseEntity<?> cadastrar(@RequestBody @Valid AlunoDto alunoDto, UriComponentsBuilder uriComponentsBuilder) {
        Aluno aluno = new Aluno(alunoDto.getEmail(), alunoDto.getNome(), alunoDto.getIdade());
        alunoRepository.save(aluno);
        URI uri = uriComponentsBuilder.path("/alunos/{id}").buildAndExpand(aluno.getId()).toUri();
        return ResponseEntity.created(uri).body(new AlunoDto(aluno));
    }

    @GetMapping
    @Cacheable(value = "listaDeAlunos") // Habilita cache para este método.
    public Page<AlunoDto> listar(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Aluno> alunos = alunoRepository.findAll(pageable);
        return AlunoDto.converter(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDetalhesDto> detalhar(@PathVariable Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        return aluno.map(value -> ResponseEntity.ok(new AlunoDetalhesDto(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaDeAlunos", allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        if (aluno.isPresent()) {
            alunoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return  ResponseEntity.notFound().build();
    }
}
