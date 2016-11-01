package main;

import java.io.IOException;
import java.util.ArrayList;

import analisadorLexico.AnalisadorLexico;
import analisadorLexico.Token;
import analisadorSintatico.AnalisadorSintatico;
import analisadorSintatico.Analise;
import arquivo.Arquivo;

/**
 * Cont�m as chamadas de m�todos necess�rios e as classes instanciadas para o uso do analisador. Executando ela uma �nica vez, ela vai ler todos os c�digo da pasta testes/in, fazer as suas an�lises e armazenar os resultados na pasta teste/out/lexico
 * 
 * @author Afonso Machado
 *
 */
public class Principal {

	/**
	 * M�todo principal do an�lisador l�xico, respons�vel por
	 * instanciar a estrutura l�xica da linguagem, verificar a existencia
	 * de c�digos fontes, ler os mesmo e realizas a sua an�lise l�xica.
	 * Ao final armazenando o resultado da an�lise em um arquivo de texto.
	 * 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Arquivo arquivo = new Arquivo();
		AnalisadorLexico lexico = new AnalisadorLexico();
		AnalisadorSintatico sintatico = new AnalisadorSintatico();
		
		ArrayList<String> codigos = new ArrayList<>(); // Recebe a lista com todos os c�digos da pasta.
        codigos = arquivo.lerCodigos();
		if (codigos.isEmpty()) { // Pasta de c�digos de entrada vazia.
            System.out.println("Sem C�digos");
            System.exit(0);
        }
		
		for(String codigo : codigos) { //pra cada c�digo
			lexico = new AnalisadorLexico();
			ArrayList<String> codigoFonte = arquivo.lerCodigoFonte(codigo);
			codigoFonte = arquivo.lerCodigoFonte(codigo);
			System.out.println("Iniciando a analise lexica...");
			lexico.analiseCodigo(codigoFonte, arquivo.getLocalFile());
			arquivo.gravaSaida(lexico.getTokens(), lexico.getErros());
			
			System.out.println("Analise Lexica feita com sucesso!\n");
			if (lexico.getErros().isEmpty()) {
				System.out.println("Nao foram encontrados erros");
			} else {
				System.out.println("Foram detectados erros - Verifique-os no arquivo de saida");
			}
			System.out.println(" ");
			
			
			sintatico = new AnalisadorSintatico();
			ArrayList<Token> listaTokens = lexico.getTokens();
			System.out.println("Iniciando a analise sintatica e semantica... ");
			System.out.println("Analisando: " + arquivo.getLocalFile());
			sintatico.analise(listaTokens);
			arquivo.gravaSaidaSintatico(sintatico.getErros());
			arquivo.gravaSaidaSemantico(sintatico.getErrosSemanticos());
			
						
		}
	}

}
