package br.com.zup.orangetalents.dto;

import br.com.zup.orangetalents.model.Aluno;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AlunoDto {

    private Long id;

    @NotNull @NotEmpty @Size(max=30)
    private String email;

    @NotNull @NotEmpty @Size(max=30)
    private String nome;

    @NotNull @Min(18)
    private Integer idade;

    public AlunoDto() {
    }

    public AlunoDto(Aluno aluno) {
        this.id = aluno.getId();
        this.email = aluno.getEmail();
        this.nome = aluno.getNome();
        this.idade = aluno.getIdade();
    }

    public static Page<AlunoDto> converter(Page<Aluno> alunos) {
        return alunos.map(AlunoDto::new);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public Integer getIdade() {
        return idade;
    }
}
