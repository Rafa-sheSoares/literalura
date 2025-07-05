package br.com.alura.literalura.controller;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    public List<Autor> listarAutores() {
        return autorService.listarTodos();
    }

    @GetMapping("/vivos/{ano}")
    public ResponseEntity<?> listarAutoresVivos(@PathVariable int ano) {
        if (ano < 0) {
            return ResponseEntity.badRequest().body("Ano inválido");
        }
        List<Autor> autores = autorService.listarAutoresVivos(ano);
        return ResponseEntity.ok(autores);
    }
}

