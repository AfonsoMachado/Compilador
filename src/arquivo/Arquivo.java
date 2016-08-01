/**
 * 
 */
package arquivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import analisadorLexico.Token;

/**
 * @author Afonso Machado
 * @author Henderson Chalegre
 *
 */
public class Arquivo {
	
	private String localFile;
	
	/**
	 * @return
	 */
	public ArrayList<String> lerCodigos() {

		ArrayList<String> codigos = new ArrayList<>(); //lista de c�digos dentro de uma pasta
        File caminho = new File("testes/in/");
        //ArrayList<String> codigos = new ArrayList<>();
        for (File f : caminho.listFiles()) {
            codigos.add(f.getName());
        }
        return codigos;
    }
	
	/**
	 * @param localFile
	 * @return
	 * @throws FileNotFoundException
	 */
	public ArrayList<String> lerCodigoFonte(String localFile) throws FileNotFoundException {

        Scanner scanner = new Scanner(new FileReader("testes/in/" + localFile)); // Lendo o arquivo do c�digo.
        this.localFile = localFile; // Guarda o nome do arquivo de entrada para que o arquivo de sa�da tenha o "mesmo" nome.
        ArrayList<String> codigo = new ArrayList<String>(); // C�digo obtido.
        while (scanner.hasNextLine()) { // Capturando as linhas do c�digo.
        	
        String s = scanner.nextLine();
        
        if (s.length() != 0){
            codigo.add(s);}
            
        }
        scanner.close();
        return codigo;
    }
	
	/**
	 * @param tokens
	 * @param erros
	 * @throws IOException
	 */
	public void gravaSaida(ArrayList<Token> tokens, ArrayList<String> erros) throws IOException {

        FileWriter arq = new FileWriter("testes/out/lexico/" + this.localFile + ".out", false); // Cria o arquivo de sa�da relacionado ao seu respectivo arquivo de entrada ("mesmo" nome). 
        PrintWriter gravar = new PrintWriter(arq);
        for (Token token : tokens) { // Insere os tokens no arquivo de sa�da.
            gravar.println("Lexema: " + token.getLexema() + " | Tipo: " + token.getTipo() + " | Linha: " + token.getLinha() + " | Coluna: " + token.getColuna());
        }
        if (erros.isEmpty()) { // Se n�o houver erros l�xicos.
            gravar.printf("\nSucesso! N�o existem erros l�xicos\n");
        } else { // Se houver erros l�xicos, os insere no arquivo de sa�da.
            for (String erro : erros) {
                gravar.println("Erro: " + erro);
            }
        }
        arq.close();
    }
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getLocalFile(){
		return this.localFile;
	}

}
