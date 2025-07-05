package br.com.alura.literalura;

import br.com.alura.literalura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private Principal principal;

	public static void main(String[] args) {
		System.out.println("Iniciando Literalura...");
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run (String... args) throws Exception {
			principal.exibeMenu();

//		new SpringApplicationBuilder(LiteraluraApplication.class)
//				.web(WebApplicationType.NONE)
//				.run(args);
//
//		Principal principal = new Principal();
//		while (true) {
//			principal.exibeMenu();
//		}

		}
}



