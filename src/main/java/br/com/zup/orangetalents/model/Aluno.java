package br.com.zup.orangetalents.model;

import javax.persistence.*;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nome;

    private Integer idade;

    public Aluno() {
    }

    public Aluno(String email, String nome, Integer idade) {
        this.email = email;
        this.nome = nome;
        this.idade = idade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }
}
