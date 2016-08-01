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

		ArrayList<String> codigos = new ArrayList<>(); //lista de códigos dentro de uma pasta
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

        Scanner scanner = new Scanner(new FileReader("testes/in/" + localFile)); // Lendo o arquivo do código.
        this.localFile = localFile; // Guarda o nome do arquivo de entrada para que o arquivo de saída tenha o "mesmo" nome.
        ArrayList<String> codigo = new ArrayList<String>(); // Código obtido.
        while (scanner.hasNextLine()) { // Capturando as linhas do código.
        	
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

        FileWriter arq = new FileWriter("testes/out/lexico/" + this.localFile + ".out", false); // Cria o arquivo de saída relacionado ao seu respectivo arquivo de entrada ("mesmo" nome). 
        PrintWriter gravar = new PrintWriter(arq);
        for (Token token : tokens) { // Insere os tokens no arquivo de saída.
            gravar.println("Lexema: " + token.getLexema() + " | Tipo: " + token.getTipo() + " | Linha: " + token.getLinha() + " | Coluna: " + token.getColuna());
        }
        if (erros.isEmpty()) { // Se não houver erros léxicos.
            gravar.printf("\nSucesso! Não existem erros léxicos\n");
        } else { // Se houver erros léxicos, os insere no arquivo de saída.
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
