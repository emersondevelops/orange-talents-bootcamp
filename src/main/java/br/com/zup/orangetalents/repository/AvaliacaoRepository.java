package br.com.zup.orangetalents.repository;

import br.com.zup.orangetalents.model.Avaliacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    Page<Avaliacao> findByTitulo (String titulo, Pageable paginacao);
}
