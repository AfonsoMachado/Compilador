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
 * Cont�m os m�todos relacionados � leitura dos c�digos fontes e � grava��o do resultado final da an�lise l�xica em arquivos de texto.
 * 
 * @author Afonso Machado
 * @author Henderson Chalegre
 *
 */
public class Arquivo {
	
	/**
	 * Guarda o nome do arquivo que est� sendo analisado
	 */
	private String localFile;
	
	/**
	 * M�todo que identifica todos os c�digos fonte
	 * que est�o armazenados na pasta de entrada.
	 * 
	 * @return codigos - C�digos fonte existentes na pasta
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
	 * L� todo o conte�do de um c�digo fonte e armazena
	 * cada linha do mesmo num ArrayList de Strings, para facilitar
	 * a an�lise do c�digo posteriormente.
	 * 
	 * @param localFile - T�tulo do c�digo a ser lido
	 * @return codigo - ArrayList de Strings com as linhas do c�digo fonte
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
	 * Grava a sa�da da an�lise l�xica num arquivo de texto
	 * para cara c�digo, contendo os tokens e os erros de cada c�digo.
	 * 
	 * @param tokens - ArrayList de tokens para serem armazenados
	 * @param erros - ArrayList de erros para serem armazenados
	 * @throws IOException
	 * @see {@link ArrayList}
	 */
	public void gravaSaida(ArrayList<Token> tokens, ArrayList<String> erros) throws IOException {

        FileWriter arq = new FileWriter("testes/out/lexico/" + this.localFile + ".out", false); // Cria o arquivo de sa�da relacionado ao seu respectivo arquivo de entrada ("mesmo" nome). 
        PrintWriter gravar = new PrintWriter(arq);
        for (Token token : tokens) { // Insere os tokens no arquivo de sa�da.
            gravar.println("Token -> Lexema: " + token.getLexema() + " | Tipo: " + token.getTipo() + " | Linha: " + token.getLinha() + " | Coluna: " + token.getColuna());
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
	
	public void gravaSaidaSintatico(ArrayList<String> erros) throws IOException {
		FileWriter arq = new FileWriter("testes/out/sintatico/" + this.localFile + ".out", false); // Cria o arquivo de sa�da relacionado ao seu respectivo arquivo de entrada ("mesmo" nome). 
        PrintWriter gravar = new PrintWriter(arq);
        if (erros.isEmpty()) { // Se n�o houver erros l�xicos.
            gravar.printf("\nSucesso! N�o existem erros Sintaticos\n");
        } else { // Se houver erros l�xicos, os insere no arquivo de sa�da.
            for (String erro : erros) {
                gravar.println("Erro: " + erro);
            }
        }
        arq.close();
	}
	
	/**
	 * 
	 * @return nome do arquivo que est� sendo analisado
	 */
	public String getLocalFile(){
		return this.localFile;
	}

}
