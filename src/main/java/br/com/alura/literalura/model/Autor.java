package br.com.alura.literalura.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @ManyToMany(mappedBy = "autores")
    private List<Livro> livros = new ArrayList<>();
    private Integer anoNascimento;
    private Integer anoFalecimento;

}
