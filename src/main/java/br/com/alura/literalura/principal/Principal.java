package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.DadosApi;
import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.AutorService;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Data
@Component
public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "https://gutendex.com/books/?page=";

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorService autorService;

    /**
     * Exibe o menu principal e chama os métodos com base na opção escolhida
     */
    public void exibeMenu() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("""
                \n=== MENU PRINCIPAL ===
                1 - Buscar Livro por nome
                2 - Buscar Livro por autor
                3 - Listar Livros por idioma
                4 - Listar todos os Livros salvos
                5 - Listar Autores vivos em um ano
                6 - Listar todos os Autore
                7 - Exibir contagem de livros por idioma
                8 - Estatisticas de Downloads
                0 - Sair
                """);

            System.out.print("Escolha uma opção: ");
            try {
                opcao = leitura.nextInt();
                leitura.nextLine(); // limpa o buffer

                switch (opcao) {
                    case 1 -> buscarLivroPorNome();
                    case 2 -> buscarLivroPorAutor();
                    case 3 -> listarLivrosPorIdioma();
                    case 4 -> listarLivrosBuscados();
                    case 5 -> listarAutoresVivosNoAno();
                    case 6 -> listarAutores();
                    case 7 -> exibirContagemLivrosPorIdioma();
                    case 8 -> mostrarEstatisticasDownloads();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, digite apenas números.");
                leitura.nextLine();
            }
        }
    }

    /**
     * Busca livros por nome e salva o primeiro autor e o livro no banco
     */
    private void buscarLivroPorNome() {
        System.out.print("Digite o nome do livro: ");
        String nomeLivro = leitura.nextLine();

        try {
            String nomeCodificado = URLEncoder.encode(nomeLivro, StandardCharsets.UTF_8);
            String json = consumo.obterDados(ENDERECO + "1&search=" + nomeCodificado);
            DadosApi resultado = new ConverteDados().obterDados(json, DadosApi.class);

            List<DadosLivro> livros = resultado.results();

            if (livros.isEmpty()) {
                System.out.println("Nenhum livro encontrado com esse nome.");
                return;
            }

            for (DadosLivro livro : livros) {
                System.out.println("\nTítulo: " + livro.titulo());
                livro.autores().forEach(a -> System.out.println("Autor: " + a.nome()));
                System.out.println("Idioma: " + livro.idiomas());
                System.out.println("Downloads: " + livro.quantidadeDownload());

                if (!livro.autores().isEmpty()) {
                    var primeiroAutor = livro.autores().get(0);

                    // vai criar e salvar autor - oq tava dando erro
                    Autor autor = new Autor();
                    autor.setNome(primeiroAutor.nome());
                    autor.setAnoNascimento(primeiroAutor.anoNascimento());
                    autor.setAnoFalecimento(primeiroAutor.anoFalecimento());
                    autorService.salvar(autor);

                    // vai criar e salvar livro
                    Livro livroSalvar = new Livro();
                    livroSalvar.setTitulo(livro.titulo());
                    livroSalvar.setIdioma(String.join(",", livro.idiomas()));
                    livroSalvar.setDownloads(livro.quantidadeDownload());
                    livroSalvar.setAutores(Collections.singletonList(autor));

                    livroRepository.save(livroSalvar);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar o livro: " + e.getMessage());
        }
    }

    /**
     * Lista todos os livros salvos no banco
     */
    private void listarLivrosBuscados() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro salvo.");
            return;
        }

        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    /**
     * Busca livros pelo nome do autor
     */
    private void buscarLivroPorAutor() {
        System.out.print("Digite o nome do autor: ");
        String nomeAutor = leitura.nextLine();

        try {
            String nomeCodificado = URLEncoder.encode(nomeAutor, StandardCharsets.UTF_8);
            String json = consumo.obterDados(ENDERECO + "1&search=" + nomeCodificado);
            DadosApi resultado = new ConverteDados().obterDados(json, DadosApi.class);

            List<DadosLivro> livros = resultado.results();
            boolean encontrou = false;

            for (DadosLivro livro : livros) {
                boolean autorBate = livro.autores().stream()
                        .anyMatch(a -> a.nome().toLowerCase().contains(nomeAutor.toLowerCase()));

                if (autorBate) {
                    encontrou = true;
                    System.out.println("\nTítulo: " + livro.titulo());
                    livro.autores().forEach(a -> System.out.println("Autor: " + a.nome()));
                    System.out.println("Idioma: " + livro.idiomas());
                    System.out.println("Downloads: " + livro.quantidadeDownload());

                    if (!livro.autores().isEmpty()) {
                        var primeiroAutor = livro.autores().get(0);

                        Autor autor = new Autor();
                        autor.setNome(primeiroAutor.nome());
                        autor.setAnoNascimento(primeiroAutor.anoNascimento());
                        autor.setAnoFalecimento(primeiroAutor.anoFalecimento());
                        autorService.salvar(autor);

                        Livro livroSalvar = new Livro();
                        livroSalvar.setTitulo(livro.titulo());
                        livroSalvar.setIdioma(String.join(",", livro.idiomas()));
                        livroSalvar.setDownloads(livro.quantidadeDownload());

                        List<Autor> listaAutores = new ArrayList<>();
                        listaAutores.add(autor);
                        livroSalvar.setAutores(listaAutores);

                        livroRepository.save(livroSalvar);
                    }
                }
            }

            if (!encontrou) {
                System.out.println("Nenhum livro encontrado para o autor informado.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar livros por autor: " + e.getMessage());
        }
    }


    /**
     * Lista livros pelo idioma informado
     */
    private void listarLivrosPorIdioma() {
        System.out.print("Digite o código do idioma - pt, en, fr e demais ");
        String idioma = leitura.nextLine();

        try {
            List<Livro> livros = livroRepository.findByIdioma(idioma);

            if (livros.isEmpty()) {
                System.out.println("Nenhum livro encontrado nesse idioma.");
            } else {
                livros.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar livros por idioma: " + e.getMessage());
        }
    }

    public void exibirContagemLivrosPorIdioma() {
        System.out.print("Digite o idioma para contar os livros - pt, en...: ");
        String idioma = leitura.nextLine();

        long contagem = livroRepository.countByIdiomaContaining(idioma);
        System.out.println("Quantidade de livros no idioma '" + idioma + "': " + contagem);
    }


    /**
     * Lista autores vivos em um ano informado
     */
    private void listarAutoresVivosNoAno() {
        System.out.print("Digite o ano: ");
        try {
            int ano = leitura.nextInt();
            leitura.nextLine();

            if (ano < 0) {
                System.out.println("Ano inválido");
                return;
            }

            List<Autor> autoresVivos = autorService.listarAutoresVivos(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado nesse ano.");
            } else {
                autoresVivos.forEach(autor ->
                        System.out.printf("Nome: %s (Nasc: %d - Falec: %s)%n",
                                autor.getNome(),
                                autor.getAnoNascimento(),
                                autor.getAnoFalecimento() == null ? "Vivo" : autor.getAnoFalecimento()));
            }

        } catch (InputMismatchException e) {
            System.out.println("Ano inválido. Digite apenas números.");
            leitura.nextLine();
        }
    }

    /**
     * Lista todos os autores salvos
     */
    private void listarAutores() {
        List<Autor> autores = autorService.listarTodos();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor cadastrado.");
            return;
        }

        autores.forEach(autor ->
                System.out.printf("Nome: %s | Nasc: %d | Falec: %s%n",
                        autor.getNome(),
                        autor.getAnoNascimento(),
                        autor.getAnoFalecimento() == null ? "Vivo" : autor.getAnoFalecimento()));
    }

    private void mostrarEstatisticasDownloads() {
        List<Livro> livros = livroRepository.findAll();

        var stats = livros.stream()
                .mapToInt(Livro::getDownloads)
                .summaryStatistics();

        System.out.printf("""
        📊 Estatísticas de Downloads:
        - Total: %d
        - Média: %.2f
        - Máximo: %d
        - Mínimo: %d
        """,
                stats.getSum(), stats.getAverage(),
                stats.getMax(), stats.getMin());
    }

}
