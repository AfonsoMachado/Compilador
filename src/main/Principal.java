package main;

import java.io.IOException;
import java.util.ArrayList;

import analisadorLexico.AnalisadorLexico;
import analisadorLexico.Token;
import analisadorSintatico.AnalisadorSintatico;
import arquivo.Arquivo;

/**
 * Contém as chamadas de métodos necessários e as classes instanciadas para o uso do analisador. Executando ela uma única vez, ela vai ler todos os código da pasta testes/in, fazer as suas análises e armazenar os resultados na pasta teste/out/lexico
 * 
 * @author Afonso Machado
 *
 */
public class Principal {

	/**
	 * Método principal do análisador léxico, responsável por
	 * instanciar a estrutura léxica da linguagem, verificar a existencia
	 * de códigos fontes, ler os mesmo e realizas a sua análise léxica.
	 * Ao final armazenando o resultado da análise em um arquivo de texto.
	 * 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Arquivo arquivo = new Arquivo();
		AnalisadorLexico lexico = new AnalisadorLexico();
		AnalisadorSintatico sintatico = new AnalisadorSintatico();
		
		ArrayList<String> codigos = new ArrayList<>(); // Recebe a lista com todos os códigos da pasta.
        codigos = arquivo.lerCodigos();
		if (codigos.isEmpty()) { // Pasta de códigos de entrada vazia.
            System.out.println("Sem Códigos");
            System.exit(0);
        }
		
		for(String codigo : codigos) { //pra cada código
			lexico = new AnalisadorLexico();
			ArrayList<String> codigoFonte = arquivo.lerCodigoFonte(codigo);
			codigoFonte = arquivo.lerCodigoFonte(codigo);
			lexico.analiseCodigo(codigoFonte, arquivo.getLocalFile());
			arquivo.gravaSaida(lexico.getTokens(), lexico.getErros());
			
			System.out.println("Análise Léxica feita com sucesso!");
			if (lexico.getErros().isEmpty()) {
				System.out.println("Não foram encontrados erros");
			} else {
				System.out.println("Foram detectados erros - Verifique-os no arquivo de saída");
			}
			System.out.println(" ");
			
			ArrayList<Token> listaTokens = lexico.getTokens();
			sintatico = new AnalisadorSintatico();
			sintatico.analise(listaTokens);
		}
	}

}
