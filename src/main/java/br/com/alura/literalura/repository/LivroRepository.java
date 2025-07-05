package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Interface para operações no banco envolvendo a entidade Livro.
 * Extende JpaRepository para métodos básicos, e define buscas específicas.
 */
public interface LivroRepository extends JpaRepository<Livro, Long> {

    /**
     * Busca um livro pelo título (ou parte dele), ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param titulo Parte do título para busca.
     * @return Um Optional contendo o livro que corresponde à busca, ou vazio se não encontrar.
     */
    Optional<Livro> findByTituloContainingIgnoreCase(String titulo);

    /**
     * Lista todos os livros escritos em um idioma específico.
     *
     * @param idioma Código do idioma, ex: "pt", "en", "es".
     * @return Lista de livros nesse idioma.
     */
    List<Livro> findByIdioma(String idioma);
    List<Livro> findByIdiomaContainingIgnoreCase(String idioma);
    long countByIdioma(String idioma);
    long countByIdiomaContaining(String idioma);


    /**
     * Busca livros que tenham autores cujo nome contenha a string passada,
     * ignorando maiúsculas e minúsculas.
     *
     * Note que o método busca dentro da lista de autores (campo 'autores') e compara
     * com o atributo 'nome' de cada autor.
     *
     * @param nome Parte do nome do autor.
     * @return Lista de livros com autores que contenham esse nome.
     */
    List<Livro> findByAutoresNomeContainingIgnoreCase(String nome);

    /**
     * Retorna os 5 livros com maior número de downloads, ou seja,
     * os mais populares.
     *
     * @return Lista dos 5 livros mais baixados.
     */
    List<Livro> findTop5ByOrderByDownloadsDesc();

    /**
     * Lista todos os livros com downloads maiores ou iguais a um valor mínimo.
     *
     * Útil para filtrar livros mais populares.
     *
     * @param minDownloads Valor mínimo de downloads para filtrar.
     * @return Lista de livros com downloads acima do valor.
     */
    List<Livro> findByDownloadsGreaterThanEqual(int minDownloads);

    /**
     * Busca livros por idioma e que tenham número mínimo de downloads.
     *
     * @param idioma Código do idioma para filtro.
     * @param minDownloads Valor mínimo de downloads.
     * @return Lista de livros que correspondem aos critérios.
     */
    @Query("SELECT l FROM Livro l WHERE l.idioma = :idioma AND l.downloads >= :minDownloads")
    List<Livro> buscarPorIdiomaEPopularidade(String idioma, int minDownloads);


}
