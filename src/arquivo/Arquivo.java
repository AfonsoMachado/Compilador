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
 * Contém os métodos relacionados à leitura dos códigos fontes e à gravação do resultado final da análise léxica em arquivos de texto.
 * 
 * @author Afonso Machado
 * @author Henderson Chalegre
 *
 */
public class Arquivo {
	
	/**
	 * Guarda o nome do arquivo que está sendo analisado
	 */
	private String localFile;
	
	/**
	 * Método que identifica todos os códigos fonte
	 * que estão armazenados na pasta de entrada.
	 * 
	 * @return codigos - Códigos fonte existentes na pasta
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
	 * Lê todo o conteúdo de um código fonte e armazena
	 * cada linha do mesmo num ArrayList de Strings, para facilitar
	 * a análise do código posteriormente.
	 * 
	 * @param localFile - Título do código a ser lido
	 * @return codigo - ArrayList de Strings com as linhas do código fonte
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
	 * Grava a saída da análise léxica num arquivo de texto
	 * para cara código, contendo os tokens e os erros de cada código.
	 * 
	 * @param tokens - ArrayList de tokens para serem armazenados
	 * @param erros - ArrayList de erros para serem armazenados
	 * @throws IOException
	 * @see {@link ArrayList}
	 */
	public void gravaSaida(ArrayList<Token> tokens, ArrayList<String> erros) throws IOException {

        FileWriter arq = new FileWriter("testes/out/lexico/" + this.localFile + ".out", false); // Cria o arquivo de saída relacionado ao seu respectivo arquivo de entrada ("mesmo" nome). 
        PrintWriter gravar = new PrintWriter(arq);
        for (Token token : tokens) { // Insere os tokens no arquivo de saída.
            gravar.println("Token -> Lexema: " + token.getLexema() + " | Tipo: " + token.getTipo() + " | Linha: " + token.getLinha() + " | Coluna: " + token.getColuna());
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
	
	public void gravaSaidaSintatico(ArrayList<String> erros) throws IOException {
		FileWriter arq = new FileWriter("testes/out/sintatico/" + this.localFile + ".out", false); // Cria o arquivo de saída relacionado ao seu respectivo arquivo de entrada ("mesmo" nome). 
        PrintWriter gravar = new PrintWriter(arq);
        if (erros.isEmpty()) { // Se não houver erros léxicos.
            gravar.printf("\nSucesso! Não existem erros Sintaticos\n");
        } else { // Se houver erros léxicos, os insere no arquivo de saída.
            for (String erro : erros) {
                gravar.println("Erro: " + erro);
            }
        }
        arq.close();
	}
	
	/**
	 * 
	 * @return nome do arquivo que está sendo analisado
	 */
	public String getLocalFile(){
		return this.localFile;
	}

}
