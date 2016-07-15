package analisadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class AnalisadorLexico {
	
    private ArrayList<String> codigos = new ArrayList<>(); //lista de c�digos dentro de uma pasta
    private ArrayList<String> codigoFonte = new ArrayList<>(); // ir� receber
    
    private ArrayList<Token> tokens = new ArrayList<>();
    private ArrayList<String> erros = new ArrayList<>();

	private static final char EOF = '\0';
	private final EstruturaLexica estruturaLexica = new EstruturaLexica();
	
	private int linha = 0;
	private int coluna = 0;
	private boolean linhaVazia;

	public static void main(String[] args) {
		
		
	}
	
	public void analiseGeral() throws FileNotFoundException{ //analisa todos os c�digos fonte
		ArrayList<String> localFiles = new ArrayList<>(); // Recebe a lista com todos os c�digos da pasta.
        localFiles = lerCodigos();
		if (localFiles.isEmpty()) { // Pasta de c�digos de entrada vazia.
            System.out.println("Sem C�digos para Compilar");
            System.exit(0);
        }
		
		for(String arquivos : localFiles) { //pra cada c�digo
			this.codigoFonte = lerCodigoFonte(arquivos);
			analiseCodigo();
			//IMPRIME OS TOKENS E OS ERROS AQUI
		}
		
	}
	
	private void analiseCodigo() {
		char c = leCaractere();
		String lexema;
		//enquanto n�o chegar no fim do arquivo
		
		while(c != EOF){
			if (!this.linhaVazia){
				lexema = "";
				
				if(Character.isSpaceChar(c))
					coluna++;
				else if (Character.isLetter(c)){
					letras(lexema, c);
				}
				else if (Character.isDigit(c)){
					this.digito(lexema, c);
				}
				// Compreende somente os operadores relacionais e aritmeticos
				else if (estruturaLexica.isOperador(c)){
					this.operador(lexema, c);
				}
				else if (estruturaLexica.isDelimitador(c)) {
					this.delimitador(lexema, c);
				}
				//VER ESSAS AS APAS SIMPLES
				else if (c == '\'') {
					this.caractere(lexema, c);
				}
				else if (c == '"') {
					
				}
				else if (c == '{') {
					this.comentario(c);
				}
				
			} 
			else {
				linhaVazia = false;
				linha++;
			}
			
			c = this.leCaractere();
			
			//FAZ TUDO
		}
		
	}
	
	private void comentario(char ch) {
		int linhaInicial = this.linha; // Linha onde se inicia a sequ�ncia.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequ�ncia.

        this.coluna++;
        
        while (ch != '}' || ch != EOF) {
        	//consome os caracteres
        	this.coluna++;
        	ch = this.leCaractere();
        }
        
        if (ch == EOF) {
        	this.addErro("Comentario", "Comentario n�o finalizado", linhaInicial, colunaInicial - 1);
        }
	}
	
	private void delimitador(String lexema, char ch) {

        int linhaInicial = this.linha; // Linha onde se inicia a sequ�ncia.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequ�ncia.

        lexema = lexema + ch; // Cria o lexema apartir da composi��o do caractere lido. 
        this.coluna++;

        Token token = new Token(linhaInicial + 1, colunaInicial + 1, "Delimitador", lexema);
        this.tokens.add(token);
    }
	
	private void caractere(String lexema, char ch) {
		int linhaInicial = this.linha; // Linha onde se inicia a sequ�ncia.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequ�ncia.
        boolean erro = false;

        lexema = lexema + ch; // Cria o lexema apartir da composi��o do caractere lido. 
        this.coluna++;
        
        ch = this.leCaractere();
        if (Character.isLetterOrDigit(ch)){
        	lexema = lexema + ch;
        	this.coluna++;
        	ch = this.leCaractere();
        	if (ch != '\'')
        		erro = true;
        	lexema = lexema + ch;
        }
        
        if (!erro) {
        	Token token;
        	token = new Token(linhaInicial + 1, colunaInicial + 1, "Caractere", lexema);
        }
        else {
        	this.addErro("Cont�m mais de um caractere", lexema, linhaInicial, colunaInicial);
        }
	}
	
	//REVISAAAAAAAAAAAAAAAAAAAAAAAAR
	private void operador(String lexema, char ch){
		int linhaInicial = this.linha; // Linha onde se inicia a sequ�ncia.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequ�ncia.
        //boolean erro = false; // Identifica se houve erro.
        boolean aritimetico = false;
        
        lexema = lexema + ch; // Cria o lexema apartir da composi��o do caractere lido. 
        this.coluna++;
        
        if (ch == '+' || ch == '/' || ch == '*'){
        	aritimetico = true;
        	ch = this.leCaractere();
        }
        
        else if (ch == '-') {
        	aritimetico = true;
        	ch = this.leCaractere();
        	//Se achar um digito depois do menos, � um numero negativo, vai para o aut�mato de numero
        	if (Character.isDigit(ch)){
        		this.digito(lexema, ch);
        		return;
        	}
        }
        
        else if (ch == '<') {
        	ch = this.leCaractere();
	        if (ch == '=' || ch == '>') {
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
        }
        
        Token token;
       	if (aritimetico)
       		token = new Token(linhaInicial + 1, colunaInicial + 1, "Operador Aritm�tico", lexema);
       	token = new Token(linhaInicial + 1, colunaInicial + 1, "Operador Relacional", lexema);
        
	}
	
	public void digito(String lexema, char ch){
		int linhaInicial = linha;
		int colunaInicial = coluna;
		boolean isPonto = false;
		boolean erro = false;
		
		lexema = lexema + ch;  // Cria o lexema apartir da composi��o do caractere lido. 
        this.coluna++;
        ch = this.leCaractere();
		
        if(lexema == "-") {
    		this.coluna--;
    	}
        
        while (!(ch == EOF || Character.isSpaceChar(ch) || estruturaLexica.isOperador(ch) || estruturaLexica.isDelimitador(ch))) {
        	if(Character.isDigit(ch)){
        		lexema = lexema + ch;
            	coluna++;
            	ch = this.leCaractere();
        	}
        	if (ch == '.' && isPonto == false){
        		lexema = lexema + ch;
            	coluna++;
            	isPonto = true;
            	ch = this.leCaractere();
        	}
        	else
        		erro = true;
        }
        if (!erro){
        	Token token;
        	token = new Token(linhaInicial + 1, colunaInicial + 1, "D�gito", lexema);
        }
        else
        	addErro("Digito Inv�lido", lexema, linhaInicial, colunaInicial);
	}
	
	//Pode ser um identificador, uma palavra reservada ou um operador l�gico
	private void letras(String lexema, char ch){
		int linhaInicial = linha;
		int colunaInicial = coluna;
		boolean erro = false;
		
		lexema = lexema + ch;
		this.coluna++;
		ch = this.leCaractere();
		//a� vai percorrer at� terminar a palavra
		while (!(ch == EOF || Character.isSpaceChar(ch) || estruturaLexica.isDelimitador(ch) || estruturaLexica.isOperador(ch))) {
			//se come�ar com uma letra ja assumete que vai ser um identifiacdor ou uma palavra reservada
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
				token = new Token(linhaInicial + 1, colunaInicial + 1, "Operador L�gico", lexema);
			else
				token = new Token(linhaInicial + 1, colunaInicial + 1, "Identificador", lexema);
			tokens.add(token);
		}
		else
			this.addErro("Identificador incorreto", lexema, linhaInicial, colunaInicial);
	}
	
	private void addErro(String tipo, String erro, int linha, int coluna) {
		erros.add(erro + " -> " + tipo + " | linha: " + (linha + 1) + " | coluna: " + (coluna + 1));
	}
	
	private char leCaractere(){
		if(!codigoFonte.isEmpty()){
			char c[] = codigoFonte.get(linha).toCharArray();
			if(c.length == coluna){ //verifica se a linha � vazia, ent�o quebra a linha
				linhaVazia = false;
				//FAZER ALGO PRA QUEBRAR A LINHA
				return ' ';
			}
			else if(c.length > coluna){
				linhaVazia = false;
				return c[coluna];
			}
			else if(codigoFonte.size() > (linha + 1)){
				linha++;
				c = codigoFonte.get(linha).toCharArray();
				coluna = 0;
				if (c.length == 0) { // Caso uma linha n�o tenha absolutamente nada, apenas um "enter".
                    this.linhaVazia = true;
                    return ' ';
                }
				return c[coluna];
			}
			else
				return EOF;
		}
		else
			return EOF;
	}

	// podem conter v�rios c�digos-fonte... l� varios codigos fonte da pasta
	public ArrayList<String> lerCodigos() {

        File caminho = new File("src/testes/");
        ArrayList<String> codigos = new ArrayList<>();
        for (File f : caminho.listFiles()) {
            codigos.add(f.getName());
        }
        return codigos;
    }
	
	public ArrayList<String> lerCodigoFonte(String localFile) throws FileNotFoundException {

        Scanner scanner = new Scanner(new FileReader("src/testes/" + localFile)); // Lendo o arquivo do c�digo.
        //this.localFile = localFile; // Guarda o nome do arquivo de entrada para que o arquivo de sa�da tenha o "mesmo" nome.
        ArrayList<String> codigo = new ArrayList<String>(); // C�digo obtido.
        while (scanner.hasNextLine()) { // Capturando as linhas do c�digo.
            codigo.add(scanner.nextLine());
        }
        scanner.close();
        return codigo;
    }
	
	

}
