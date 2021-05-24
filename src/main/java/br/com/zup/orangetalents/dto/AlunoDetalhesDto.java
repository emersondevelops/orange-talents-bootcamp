package br.com.zup.orangetalents.dto;

import br.com.zup.orangetalents.model.Aluno;

public class AlunoDetalhesDto {

    private final String nome;
    private final String email;

    public AlunoDetalhesDto(Aluno aluno) {
        this.nome = aluno.getNome();
        this.email = aluno.getEmail();
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
