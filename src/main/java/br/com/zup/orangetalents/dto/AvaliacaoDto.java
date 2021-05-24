package br.com.zup.orangetalents.dto;

import br.com.zup.orangetalents.model.Avaliacao;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AvaliacaoDto {

    private Long id;

    @NotNull @NotEmpty @Size(min=4)
    private String titulo;

    @NotNull @NotEmpty @Size(min=4)
    private String descricao;

    public AvaliacaoDto() {
    }

    public AvaliacaoDto(Avaliacao avaliacao) {
        this.id = avaliacao.getId();
        this.titulo = avaliacao.getTitulo();
        this.descricao = avaliacao.getDescricao();
    }

    public static Page<AvaliacaoDto> converter(Page<Avaliacao> alunos) {
        return alunos.map(AvaliacaoDto::new);
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

}
