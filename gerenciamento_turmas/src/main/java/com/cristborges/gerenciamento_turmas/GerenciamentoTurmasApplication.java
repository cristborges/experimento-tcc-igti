package com.cristborges.gerenciamento_turmas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class GerenciamentoTurmasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerenciamentoTurmasApplication.class, args);
	}
}
