package br.com.alura.literalura.service;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivos(int ano) {
        return autorRepository.findAutoresVivosEmAno(ano);
    }
    public void salvar(Autor autor) {
        autorRepository.save(autor);
    }

}
