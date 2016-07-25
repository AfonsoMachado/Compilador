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
			lexico.analiseCodigo(codigoFonte, arquivo.getLocalFile());
			arquivo.gravaSaida(lexico.getTokens(), lexico.getErros());
			
			System.out.println("An�lise L�xica feita com sucesso!");
			System.out.println(" ");
		}
	}

}
