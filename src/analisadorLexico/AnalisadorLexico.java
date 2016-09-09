package analisadorLexico;

import java.util.ArrayList;

import arquivo.Arquivo;

/**
 *  é instanciada e executada uma vez para cada código encontrado, ela trata e armazena os tokens e erros encontrados em cada código que por fim são armazenados em um arquivo através da classe Arquivo.java
 * 
 * @see Arquivo
 * @author Afonso Machado
 * @author Henderson Chalegre
 *
 */
public class AnalisadorLexico {
	
	
    /**
     * Array que armazena o codigo fonte que está sendo análisado 
     */
    private ArrayList<String> codigoFonte = new ArrayList<>(); // irá receber
    /**
     * Array que armazena todos os tokens encontrados em um código
     */
    private ArrayList<Token> tokens = new ArrayList<>();
	/**
     * Array que armazena todos os erros encontrados em um código
     */
    private ArrayList<String> erros = new ArrayList<>();
    /**
     * Constante que define o fim de um arquivo
     */
	private static final char EOF = '\0';
	/**
	 * Instancia da classe EstrutraLexica para ser usada na analise léxica
	 * 
	 * @see EstruturaLexica
	 */
	private final EstruturaLexica estruturaLexica = new EstruturaLexica();
	/**
	 * Linha percorrida no momento no código fonte
	 * 
	 * @see codigoFonte
	 */
	private int linha = 0;
	/**
	 * Coluna percorrida no momento no código fonte
	 * 
	 * @see codigoFonte
	 */
	private int coluna = 0;
	/**
	 * Identifica se uma linha está vazia ou não.
	 */
	private boolean linhaVazia = false;
	/**
	 * Identifica a existência de uma expressão digito - digito,
	 * separando os digitos do operador.
	 */
	private boolean numeroAntes = false;
	
	/**
	 * Método que faz a análise léxica por completo de um código fonte
	 * 
	 * @param codigo - código fonte a ser analisado
	 * @param nomeDoArquivo - titulo do arquivo aonde está o código fonte
	 */
	public void analiseCodigo(ArrayList<String> codigo, String nomeDoArquivo) {
		System.out.println("Analisando: " + nomeDoArquivo);
		this.codigoFonte = codigo;
		String lexema;
		char c = leCaractere();
		
		//enquanto não chegar no fim do arquivo
		while(c != EOF){
			if (!this.linhaVazia){
				lexema = "";
				
				if (c == 'ª' || c == 'º') {
					this.caractereInvalido(lexema, c);
				}
				
				else if(estruturaLexica.isSpace(c)) {
					coluna++;
				}
				
				//Autômato para tratamento de letras maiúsculas e minúsculas
				else if (estruturaLexica.isLetra(c)){
					letras(lexema, c);
				}
				
				//Autômato para tratamento de digitos de 0 a 9
				else if (Character.isDigit(c)){
					this.digito(lexema, c);
				}

				//Autômato para tratamento de operadores relacionais e aritiméticos
				else if (estruturaLexica.isOperador(c)){
					this.operador(lexema, c);
				}
				
				//Autômato para tratamento de delmitadores
				else if (estruturaLexica.isDelimitador(c)) {
					this.delimitador(lexema, c);
				}
				
				//Autômato para tratamento de caractere
				else if (c == '\'') {
					this.caractere(lexema, c);
				}
				
				//Autômato para tratamento de cadeia de caracteres
				else if (c == '"') {
					cadeiaDeCaracteres(lexema, c);
				}
				
				//Autômato para tratamento de comentario
				else if (c == '{') {
					this.comentario(lexema, c);
				} 
				else {
					this.caractereInvalido(lexema, c);
				}
				
			} 
			else {
				linhaVazia = false;
				linha++;
			}
			
			c = this.leCaractere();

		}
		
	}
	
	/**
	 * Trata a existência de um caractere inválido encontrado no código,
	 * esses caracteres inválidos são representados na tabela ASCII dos 
	 * números 32 ao 126
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void caractereInvalido(String lexema, char ch) {
		int linhaInicial = this.linha; // Linha onde se inicia a sequência.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequência.
        
        while (!(ch == EOF || Character.isSpaceChar(ch) || estruturaLexica.isOperador(ch) || estruturaLexica.isDelimitador(ch) || ch == '\'' || ch == '"' || ch == '{')) {
        	lexema = lexema + ch;
        	this.coluna++;
        	ch = this.leCaractere();
        }
        
        this.addErro("Sequência Inválida", lexema, linhaInicial, colunaInicial);
	}
	
	/**
	 * Método que trata a existência de uma cadeia de caracteres no código.
	 * Sendo que sempre está delimitada por aspas duplas ("), e deve respeitar
	 * a seguinte formação: "letra | numero | ASCII 32"
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void cadeiaDeCaracteres(String lexema, char ch) {
		int linhaInicial = this.linha; // Linha onde se inicia a sequência.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequência.
        boolean erro = false;

        lexema = lexema + ch;
        this.coluna++;
        ch = this.leCaractere();
        
        if (!(Character.isLetter(ch))){
        	erro = true;
        }
        
        while (ch != '"' && linha == linhaInicial && ch != EOF) {
        	if (Character.isLetterOrDigit(ch)) {
        		this.coluna++;
        		lexema = lexema + ch;
        		ch = this.leCaractere();
        	} 
        	else if (Character.isSpaceChar(ch)) {
                this.coluna++;
                ch = this.leCaractere();
                if (ch == ' ' && linhaInicial == this.linha) { // Se o próximo caractere for espaço e se mantém na linha atual, o espaço anterior é inserido ao lexema. 
                    lexema += " ";
                } else if (ch != EOF && linhaInicial == this.linha) {
                    lexema += " "; // Já se sabe que o caractere lido não representa quebra de linha, logo, o espaço anterior é inserido ao lexema. 
                    if (ch != '"') { // Se o próximo caractere não for espaço nem aspas duplas e estiver na linha atual, o espaço anterior e este novo caractere são inseridos ao lexema. 
                        lexema += ch;
                        this.coluna++;
                        ch = this.leCaractere();
                    }
                }
            }
        	else {
        		this.coluna++;
        		lexema = lexema + ch;
        		ch = this.leCaractere();
        		erro = true;
        	}
        }
        //se o ultimo nçao for um ".. dá erro
        
        if (ch == '"' && linhaInicial == this.linha) {  // Verifica se a cadeia constante foi fechada na mesma linha.
            lexema += ch;
            this.coluna++;
        }
        
        if (!erro  && linhaInicial == this.linha) {
        	Token token;
        	token = new Token(linhaInicial + 1, colunaInicial + 1, "Cadeia de caracteres", lexema);
        	this.tokens.add(token);
        }
        else {
        	this.addErro("Cadeia de caracteres mal formada", lexema, linhaInicial + 1, colunaInicial + 1);
        }   
	}

	/**
	 * Trata a existência de um comentário no código. Sendo que os
	 * comentários podem ser de linha ou de bloco, contendo quaisquer
	 * caracteres, desde que sejam limitados por chaves ({}).
	 * Tudo que esteja delimitado por chaves é ignorado pela
	 * análise léxica. Caso não sejam fechadas as chaves (}),
	 * o código todo a partir do abre chaves é ignorado ({)
	 * 
	 * @param ch - Caractere inicial do comentário: {
	 */
	private void comentario(String lexema, char ch) {
		int linhaInicial = this.linha; // Linha onde se inicia a sequência.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequência.

        lexema = lexema + ch;
        this.coluna++;
        ch = this.leCaractere();
        
        while (ch != '}' && ch != EOF) {
        	//consome os caracteres
        	this.coluna++;
        	lexema = lexema + ch;
        	ch = this.leCaractere();
        }
        
        this.coluna++;
        lexema = lexema + ch;
        
        if (ch == EOF) {
        	this.addErro("Comentario", "Comentario não finalizado", linhaInicial, colunaInicial - 1);
        }
        else {
        	Token token = new Token(linhaInicial + 1, colunaInicial + 1, "Comentário", lexema);
            this.tokens.add(token);
        }
	}
	
	/**
	 * Trata a existência de delimitadores no código fonte,
	 * que são: , ; ( e ).
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void delimitador(String lexema, char ch) {

        int linhaInicial = this.linha; // Linha onde se inicia a sequência.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequência.

        lexema = lexema + ch; // Cria o lexema apartir da composição do caractere lido. 
        this.coluna++;

        Token token = new Token(linhaInicial + 1, colunaInicial + 1, "Delimitador", lexema);
        this.tokens.add(token);
    }
	
	
	/**
	 * Trata a existência de um caractere no código fonte,
	 * este caractere deve ser delimitado por aspas simples ('),
	 * respeitando a seguinte condição: 'letra | dígito'. 
	 * Casso exista mais de um caractere delimitado por aspas simples,
	 * é considerado um erro léxico.
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void caractere(String lexema, char ch) {
		int linhaInicial = this.linha; // Linha onde se inicia a sequência.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequência.
        boolean erro = false;

        lexema = lexema + ch; // Cria o lexema apartir da composição do caractere lido. 
        this.coluna++;
        ch = this.leCaractere();
        
        if (Character.isLetterOrDigit(ch)){
        	lexema = lexema + ch;
        	this.coluna++;
        	ch = this.leCaractere();
        	if (ch != '\'')
        		erro = true;
        	else {
        		lexema = lexema + ch;
        		this.coluna++;
        	}
        } else {
        	erro = true;
        }
        
        if (!erro) {
        	Token token;
        	token = new Token(linhaInicial + 1, colunaInicial + 1, "Caractere", lexema);
        	tokens.add(token);
        }
        else {
        	while ((ch != '\'' && linhaInicial == this.linha) ) {
        		lexema = lexema + ch;
            	this.coluna++;
            	ch = this.leCaractere();
        	}
        	if (linhaInicial == this.linha) {
        		lexema = lexema + ch;
            	this.coluna++;
        	}
           
        	this.addErro("Caractere Inválido", lexema, linhaInicial, colunaInicial);
        }
	}
	
	/**
	 * Método que trata a existência de um operador aritimético
	 * ou relacional no código fonte, trata tambem se o operador
	 * aritimético '-', pode gerar um número negativo
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void operador(String lexema, char ch){
		int linhaInicial = this.linha; // Linha onde se inicia a sequência.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequência.
        //boolean erro = false; // Identifica se houve erro.
        boolean aritimetico = false;
        
        lexema = lexema + ch; // Cria o lexema apartir da composição do caractere lido. 
        this.coluna++;
        
        if (ch == '+' || ch == '/' || ch == '*'){
        	aritimetico = true;
        	ch = this.leCaractere();
        }
        
        else if (ch == '-' && !(numeroAntes)) {
        	aritimetico = true;
        	ch = this.leCaractere();
        	//Se achar um digito depois do menos, é um numero negativo, vai para o autômato de numero
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
        
        else if (ch == '-' && numeroAntes) {
        	//ch = this.leCaractere();
        	//lexema = lexema + ch;
            //this.coluna++;
        }
        
        Token token;
       	if (aritimetico)
       		token = new Token(linhaInicial + 1, colunaInicial + 1, "Operador Aritmético", lexema);
       	else
       		token = new Token(linhaInicial + 1, colunaInicial + 1, "Operador Relacional", lexema);
       	
       	tokens.add(token);
        
	}
	
	/**
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void digito(String lexema, char ch){
		int linhaInicial = linha;
		int colunaInicial = coluna;
		boolean isPonto = false;
		boolean erro = false;
		
		lexema = lexema + ch;  // Cria o lexema apartir da composição do caractere lido. 
        this.coluna++;
        ch = this.leCaractere();
		
        if(lexema == "-") {
    		colunaInicial--;
    	}
        
        while (!(ch == EOF || Character.isSpaceChar(ch) || estruturaLexica.isOperador(ch) || estruturaLexica.isDelimitador(ch) || ch == '\'' || ch == '"' || ch == '{')) {
        	
        	if(!(Character.isDigit(ch)) && ch != '.') {
            	erro = true;
            	lexema = lexema + ch;
            	coluna++;
            	ch = this.leCaractere();
            }
        	else if(Character.isDigit(ch)){
        		lexema = lexema + ch;
            	coluna++;
            	ch = this.leCaractere();
        	}
        	else if (ch == '.' && isPonto == false){
        		lexema = lexema + ch;
            	coluna++;
            	isPonto = true;
            	ch = this.leCaractere();
            	if (!(Character.isDigit(ch))) {
            		erro = true;
            	}
        	}
        	else {
        		erro = true;
        		lexema = lexema + ch;
            	coluna++;
            	ch = this.leCaractere();
        	}
        }
        /*if (ch == '-') {
        	this.numeroAntes = true;
        }*/
        if (!erro){
        	Token token;
        	if (lexema.length() > 1)
        		token = new Token(linhaInicial + 1, colunaInicial + 1, "Número", lexema);
        	else
        		token = new Token(linhaInicial + 1, colunaInicial + 1, "Dígito", lexema);
        	tokens.add(token);
        }
        else
        	addErro("Número Inválido", lexema, linhaInicial, colunaInicial);
	}
	
	//Pode ser um identificador, uma palavra reservada ou um operador lógico
	/**
	 * Identifica quaisquer tokens e começam com uma letra,
	 * podendo ser um identificador, uma palavra reservada
	 * ou um operador lógico
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void letras(String lexema, char ch){
		int linhaInicial = linha;
		int colunaInicial = coluna;
		boolean erro = false;
		
		lexema = lexema + ch;
		this.coluna++;
		ch = this.leCaractere();
		//aí vai percorrer até terminar a palavra
		while (!(ch == EOF || Character.isSpaceChar(ch) || estruturaLexica.isDelimitador(ch) || estruturaLexica.isOperador(ch) || ch == '\'' || ch == '"' || ch == '{')) {
			//se começar com uma letra ja assumete que vai ser um identifiacdor ou uma palavra reservada
			if (ch == 'ª' || ch == 'º') {
				erro = true;
			}
			if(!(ch == '_' || estruturaLexica.isLetra(ch) || Character.isDigit(ch))){
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
	
	/**
	 * Adiciona um erro encontrado numa lista de erros,
	 * contendo todas as informações do mesmo.
	 * 
	 * @param tipo - Tipo do erro
	 * @param erro - Descrição do erro
	 * @param linha - Linha do erro
	 * @param coluna - Coluna do erro
	 */
	private void addErro(String tipo, String erro, int linha, int coluna) {
		erros.add(erro + " -> " + tipo + " | linha: " + (linha + 1) + " | coluna: " + (coluna + 1));
	}
	
	/**
	 * Lê um caractere por vez para a análise do código
	 * 
	 * @return caractere - Próximo caractere a ser lido para a análise
	 */
	private char leCaractere(){
		if(!codigoFonte.isEmpty()){
			char c[] = codigoFonte.get(linha).toCharArray();
			if(c.length == coluna){ //verifica se a linha é vazia, então quebra a linha
				linhaVazia = false;
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
				if (c.length == 0) { // Caso uma linha não tenha absolutamente nada, apenas um "enter".
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
	
	/**
	 * Retorna um ArrayList dos tokens existentes no código
	 * 
	 * @return Tokens existentes
	 */
	public ArrayList<Token> getTokens() {
		return tokens;
	}

	/**
	 * Retorna um ArrayList com os erros encontrados no código
	 * 
	 * @return
	 */
	public ArrayList<String> getErros() {
		return erros;
	}
	

}
