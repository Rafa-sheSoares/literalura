package br.com.alura.literalura.service;

import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Camada de serviço para regras de negócio relacionadas a livros.
 */
@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    /**
     * Lista todos os livros cadastrados.
     */
    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    /**
     * Lista livros filtrando por idioma.
     */
    public List<Livro> listarPorIdioma(String idioma) {
        return livroRepository.findByIdiomaContainingIgnoreCase(idioma);
    }

    /**
     * Busca livros que tenham autores com nomes parecidos.
     */
    public List<Livro> buscarPorAutor(String nomeAutor) {
        return livroRepository.findByAutoresNomeContainingIgnoreCase(nomeAutor);
    }

    /**
     * Busca livro por parte do título.
     */
    public Optional<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    /**
     * Retorna os 5 livros mais populares (mais baixados).
     */
    public List<Livro> top5MaisBaixados() {
        return livroRepository.findTop5ByOrderByDownloadsDesc();
    }

    /**
     * Lista livros com downloads acima de um valor mínimo.
     */
    public List<Livro> popularesComMinimoDownloads(int minDownloads) {
        return livroRepository.findByDownloadsGreaterThanEqual(minDownloads);
    }

    /**
     * Lista livros por idioma e downloads mínimos.
     */
    public List<Livro> buscarPorIdiomaEPopularidade(String idioma, int minDownloads) {
        return livroRepository.buscarPorIdiomaEPopularidade(idioma, minDownloads);
    }

    /**
     * Salva um livro no banco.
     */
    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }
}
