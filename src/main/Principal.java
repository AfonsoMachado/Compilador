/**
 * 
 */
package main;

import java.io.IOException;
import java.util.ArrayList;

import analisadorLexico.AnalisadorLexico;
import arquivo.Arquivo;

/**
 * @author Afonso Machado
 *
 */
public class Principal {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Arquivo arquivo = new Arquivo();
		AnalisadorLexico lexico = new AnalisadorLexico();
		
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
			System.out.println(" ");
		}
	}

}
