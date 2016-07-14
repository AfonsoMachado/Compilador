package analisadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class AnalisadorLexico {
	
    private ArrayList<String> codigos = new ArrayList<>(); //lista de códigos dentro de uma pasta
    private ArrayList<String> codigoFonte = new ArrayList<>(); // irá receber
    private ArrayList<Token> tokens = new ArrayList<>();
    private ArrayList<String> erros = new ArrayList<>();

	private static final char EOF = '\0';
	private final EstruturaLexica estruturaLexica = new EstruturaLexica();
	
	private int linha = 0;
	private int coluna = 0;
	private boolean linhaVazia;

	public static void main(String[] args) {
		
		
	}
	
	public void analiseGeral() throws FileNotFoundException{ //analisa todos os códigos fonte
		ArrayList<String> localFiles = new ArrayList<>(); // Recebe a lista com todos os códigos da pasta.
        localFiles = lerCodigos();
		if (localFiles.isEmpty()) { // Pasta de códigos de entrada vazia.
            System.out.println("Sem Códigos para Compilar");
            System.exit(0);
        }
		
		for(String arquivos : localFiles) { //pra cada código
			this.codigoFonte = lerCodigoFonte(arquivos);
			analiseCodigo();
		}
		
	}
	
	private void analiseCodigo() {
		char c = leCaractere();
		String lexema = "";
		//enquanto não chegar no fim do arquivo
		while(c != EOF){
			
			if(Character.isSpaceChar(c))
				coluna++;
			else if (Character.isLetter(c)){
				letras(lexema, c);
			}
			else if (Character.isDigit(c)){
				this.numero(lexema, c);
			}
			// Compreende somente os operadores relacionais e aritmeticos
			else if (estruturaLexica.isOperador(c)){
				
			}
			
			//FAZ TUDO
		}
		
	}
	
	private void operador(String lexema, char ch){
		int linhaInicial = this.linha; // Linha onde se inicia a sequência.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequência.
        boolean erro = false; // Identifica se houve erro.
        boolean aritimetico = false;
        
        lexema = lexema + ch; // Cria o lexema apartir da composição do caractere lido. 
        this.coluna++;
        
        //CONSIDERA SE O USUARIO COLOCAR ++ -- ** //
        if (ch == '+' || ch == '-' || ch == '*' || ch == '-'){
        	aritimetico = true;
        	ch = this.leCaractere();
        	if (estruturaLexica.isOperador(ch)){
        		erro = true;
        		this.coluna++;
        		lexema = lexema + ch;
        	}
        }
        
        else if (ch == '<') {
        	ch = this.leCaractere();
	        if (ch == '=' || ch == '>') {
	            lexema = lexema + ch;
	            this.coluna++;
	        }
	        else if(estruturaLexica.isOperadorAritimetico(ch)) {
	        	erro = true;
	        	lexema = lexema + ch;
	        	this.coluna++;
	        }
        }
        
        else if (ch == '>') {
        	ch = this.leCaractere();
	        if (ch == '=') {
	            lexema = lexema + ch;
	            this.coluna++;
	        }
	        else if(estruturaLexica.isOperadorAritimetico(ch) || ch == '>' || ch == '<') {
	        	erro = true;
	        	lexema = lexema + ch;
	        	this.coluna++;
	        }
        }
        
        
        
        
        
        
        if(!erro){
        	Token token;
        	if (aritimetico)
        		token = new Token(linhaInicial + 1, colunaInicial + 1, "Operador Aritmético", lexema);
        	token = new Token(linhaInicial + 1, colunaInicial + 1, "Operador Relacional", lexema);
		}
		else
			this.addErro("Operador Inexistente", lexema, linhaInicial, colunaInicial);
        
	}
	
	
	private void numero(String lexema, char ch){
		int linhaInicial = linha;
		int colunaInicial = coluna;
		
		lexema = lexema + ch;  // Cria o lexema apartir da composição do caractere lido. 
        this.coluna++;
        ch = this.leCaractere();
		//COLOCAR AQUI O AUTÔMATO DE DIGITO
	}
	
	//Pode ser um identificador, uma palavra reservada ou um operador lógico
	private void letras(String lexema, char ch){
		int linhaInicial = linha;
		int colunaInicial = coluna;
		boolean erro = false;
		
		lexema = lexema + ch;
		this.coluna++;
		ch = this.leCaractere();
		//aí vai percorrer até terminar a palavra
		while (!(ch == EOF || Character.isSpaceChar(ch) || estruturaLexica.isDelimitador(ch) || estruturaLexica.isOperador(ch))) {
			//se começar com uma letra ja assumete que vai ser um identifiacdor ou uma palavra reservada
			if(!(ch == '_' || Character.isLetterOrDigit(ch))){
				erro = true;
			}
			lexema = lexema + ch;
			coluna++;
			ch = this.leCaractere();
		}
		
		if(!erro){
			Token token;
			if(estruturaLexica.isPalavraResevada(lexema))
				token = new Token(linhaInicial + 1, colunaInicial + 1, "Palavra Reservada", lexema);
			else if (estruturaLexica.isOperadorLogico(lexema))
				token = new Token(linhaInicial + 1, colunaInicial + 1, "Operador Lógico", lexema);
			else
				token = new Token(linhaInicial + 1, colunaInicial + 1, "Identificador", lexema);
			tokens.add(token);
		}
		else
			this.addErro("Identificador incorreto", lexema, linhaInicial, colunaInicial);
	}
	
	private void addErro(String tipo, String erro, int linha, int coluna) {
		//VERIFICA SE É LINHA E COLINA + 1
		erros.add(erro + " -> " + tipo + " | linha: " + (linha + 1) + " | coluna: " + (coluna + 1));
	}
	
	private char leCaractere(){
		if(!codigoFonte.isEmpty()){
			char c[] = codigoFonte.get(linha).toCharArray();
			if(c.length == coluna){ //verifica se a linha é vazia, então quebra a linha
				linhaVazia = false;
				//FAZER ALGO PRA QUEBRAR A LINHA
				return 0;
			}
			else if(c.length > coluna){
				linhaVazia = false;
				return c[coluna];
			}
			else if(codigoFonte.size() > (linha + 1)){
				linha++;
				c = codigoFonte.get(linha).toCharArray();
				coluna = 0;
				return c[coluna];
			}
			else
				return EOF;
		}
		else
			return EOF;
	}

	// podem conter vários códigos-fonte... lê varios codigos fonte da pasta
	public ArrayList<String> lerCodigos() {

        File caminho = new File("src/testes/");
        ArrayList<String> codigos = new ArrayList<>();
        for (File f : caminho.listFiles()) {
            codigos.add(f.getName());
        }
        return codigos;
    }
	
	public ArrayList<String> lerCodigoFonte(String localFile) throws FileNotFoundException {

        Scanner scanner = new Scanner(new FileReader("src/testes/" + localFile)); // Lendo o arquivo do código.
        //this.localFile = localFile; // Guarda o nome do arquivo de entrada para que o arquivo de saída tenha o "mesmo" nome.
        ArrayList<String> codigo = new ArrayList<String>(); // Código obtido.
        while (scanner.hasNextLine()) { // Capturando as linhas do código.
            codigo.add(scanner.nextLine());
        }
        scanner.close();
        return codigo;
    }
	
	

}
