package br.com.alura.literalura.controller;

import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para endpoints relacionados a livros.
 * Permite listar, buscar por idioma, autor, popularidade, etc.
 */
@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    /**
     * Lista todos os livros cadastrados.
     */
    @GetMapping
    public List<Livro> listarTodos() {
        return livroService.listarTodos();
    }

    /**
     * Lista livros pelo idioma informado (ex: "pt", "en").
     */
    @GetMapping("/idioma/{idioma}")
    public ResponseEntity<?> listarPorIdioma(@PathVariable String idioma) {
        if (idioma == null || idioma.isBlank()) {
            return ResponseEntity.badRequest().body("Idioma inválido");
        }
        var livros = livroService.listarPorIdioma(idioma);
        return ResponseEntity.ok(livros);
    }

    /**
     * Busca livros com nome de autor que contenha a string informada.
     */
    @GetMapping("/autor/{nome}")
    public ResponseEntity<List<Livro>> buscarPorAutor(@PathVariable String nome) {
        var livros = livroService.buscarPorAutor(nome);
        return ResponseEntity.ok(livros);
    }

    /**
     * Retorna os 5 livros mais baixados.
     */
    @GetMapping("/populares/top5")
    public List<Livro> top5MaisBaixados() {
        return livroService.top5MaisBaixados();
    }

    /**
     * Lista livros com número mínimo de downloads.
     */
    @GetMapping("/populares")
    public List<Livro> popularesPorMinDownloads(@RequestParam(defaultValue = "100") int min) {
        return livroService.popularesComMinimoDownloads(min);
    }

    /**
     * Lista livros por idioma e número mínimo de downloads.
     */
    @GetMapping("/filtro")
    public List<Livro> buscarPorIdiomaEPopularidade(
            @RequestParam String idioma,
            @RequestParam(defaultValue = "100") int minDownloads) {
        return livroService.buscarPorIdiomaEPopularidade(idioma, minDownloads);
    }

    /**
     * Busca um livro por parte do título.
     */
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<?> buscarPorTitulo(@PathVariable String titulo) {
        return livroService.buscarPorTitulo(titulo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
