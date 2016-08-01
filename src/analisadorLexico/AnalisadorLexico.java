/**
 * 
 */
package analisadorLexico;

import java.util.ArrayList;

/**
 * @author Afonso Machado
 * @author Henderson Chalegre
 *
 */
public class AnalisadorLexico {
	
	
    /**
     * Array que armazena o codigo fonte que est� sendo an�lisado 
     */
    private ArrayList<String> codigoFonte = new ArrayList<>(); // ir� receber
    /**
     * Array que armazena todos os tokens encontrados em um c�digo
     */
    private ArrayList<Token> tokens = new ArrayList<>();
	/**
     * Array que armazena todos os erros encontrados em um c�digo
     */
    private ArrayList<String> erros = new ArrayList<>();
    /**
     * Constante que define o fim de um arquivo
     */
	private static final char EOF = '\0';
	/**
	 * Instancia da classe EstrutraLexica para ser usada na analise l�xica
	 * 
	 * @see EstruturaLexica
	 */
	private final EstruturaLexica estruturaLexica = new EstruturaLexica();
	/**
	 * Linha percorrida no momento no c�digo fonte
	 * 
	 * @see codigoFonte
	 */
	private int linha = 0;
	/**
	 * Coluna percorrida no momento no c�digo fonte
	 * 
	 * @see codigoFonte
	 */
	private int coluna = 0;
	/**
	 * Identifica se uma linha est� vazia ou n�o.
	 */
	private boolean linhaVazia = false;
	/**
	 * Identifica a exist�ncia de uma express�o digito - digito,
	 * separando os digitos do operador.
	 */
	private boolean numeroAntes = false;
	
	/**
	 * M�todo que faz a an�lise l�xica por completo de um c�digo fonte
	 * 
	 * @param codigo - c�digo fonte a ser analisado
	 * @param nomeDoArquivo - titulo do arquivo aonde est� o c�digo fonte
	 */
	public void analiseCodigo(ArrayList<String> codigo, String nomeDoArquivo) {
		System.out.println("Analisando: " + nomeDoArquivo);
		this.codigoFonte = codigo;
		String lexema;
		char c = leCaractere();
		
		//enquanto n�o chegar no fim do arquivo
		while(c != EOF){
			if (!this.linhaVazia){
				lexema = "";
				
				if(estruturaLexica.isSpace(c)) {
					coluna++;
				}
				
				//Aut�mato para tratamento de letras mai�sculas e min�sculas
				else if (Character.isLetter(c)){
					letras(lexema, c);
				}
				
				//Aut�mato para tratamento de digitos de 0 a 9
				else if (Character.isDigit(c)){
					this.digito(lexema, c);
				}

				//Aut�mato para tratamento de operadores relacionais e aritim�ticos
				else if (estruturaLexica.isOperador(c)){
					this.operador(lexema, c);
				}
				
				//Aut�mato para tratamento de delmitadores
				else if (estruturaLexica.isDelimitador(c)) {
					this.delimitador(lexema, c);
				}
				
				//Aut�mato para tratamento de caractere
				else if (c == '\'') {
					this.caractere(lexema, c);
				}
				
				//Aut�mato para tratamento de cadeia de caracteres
				else if (c == '"') {
					cadeiaDeCaracteres(lexema, c);
				}
				
				//Aut�mato para tratamento de comentario
				else if (c == '{') {
					this.comentario(c);
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
	 * Trata a exist�ncia de um caractere inv�lido encontrado no c�digo,
	 * esses caracteres inv�lidos s�o representados na tabela ASCII dos 
	 * n�meros 32 ao 126
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void caractereInvalido(String lexema, char ch) {
		int linhaInicial = this.linha; // Linha onde se inicia a sequ�ncia.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequ�ncia.
        
        while (!(ch == EOF || Character.isSpaceChar(ch) || estruturaLexica.isOperador(ch) || estruturaLexica.isDelimitador(ch) || ch == '\'' || ch == '"' || ch == '{')) {
        	lexema = lexema + ch;
        	this.coluna++;
        	ch = this.leCaractere();
        }
        
        this.addErro("Sequ�ncia Inv�lida", lexema, linhaInicial, colunaInicial);
	}
	
	/**
	 * M�todo que trata a exist�ncia de uma cadeia de caracteres no c�digo.
	 * Sendo que sempre est� delimitada por aspas duplas ("), e deve respeitar
	 * a seguinte forma��o: "letra | numero | ASCII 32"
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void cadeiaDeCaracteres(String lexema, char ch) {
		int linhaInicial = this.linha; // Linha onde se inicia a sequ�ncia.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequ�ncia.
        boolean erro = false;

        lexema = lexema + ch;
        this.coluna++;
        ch = this.leCaractere();
        
        if (!(Character.isLetter(ch))){
        	erro = true;
        }
        
        while (ch != '"' && ch != EOF) {
        	this.coluna++;
        	if (Character.isLetterOrDigit(ch) || Character.isSpaceChar(ch)) {
        		lexema = lexema + ch;
        		ch = this.leCaractere();
        	} 
        	else {
        		lexema = lexema + ch;
        		ch = this.leCaractere();
        		erro = true;
        	}
        }
        
        this.coluna++;
        lexema = lexema + ch;
        
        if (!erro) {
        	Token token;
        	token = new Token(linhaInicial + 1, colunaInicial + 1, "Cadeia de caracteres", lexema);
        	this.tokens.add(token);
        }
        else {
        	this.addErro("Cadeia de caracteres mal formada", lexema, linhaInicial + 1, colunaInicial + 1);
        }   
	}

	/**
	 * Trata a exist�ncia de um coment�rio no c�digo. Sendo que os
	 * coment�rios podem ser de linha ou de bloco, contendo quaisquer
	 * caracteres, desde que sejam limitados por chaves ({}).
	 * Tudo que esteja delimitado por chaves � ignorado pela
	 * an�lise l�xica. Caso n�o sejam fechadas as chaves (}),
	 * o c�digo todo a partir do abre chaves � ignorado ({)
	 * 
	 * @param ch - Caractere inicial do coment�rio: {
	 */
	private void comentario(char ch) {
		int linhaInicial = this.linha; // Linha onde se inicia a sequ�ncia.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequ�ncia.

        this.coluna++;
        
        while (ch != '}' && ch != EOF) {
        	//consome os caracteres
        	this.coluna++;
        	ch = this.leCaractere();
        }
        
        this.coluna++;
        
        if (ch == EOF) {
        	this.addErro("Comentario", "Comentario n�o finalizado", linhaInicial, colunaInicial - 1);
        }
	}
	
	/**
	 * Trata a exist�ncia de delimitadores no c�digo fonte,
	 * que s�o: , ; ( e ).
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
	private void delimitador(String lexema, char ch) {

        int linhaInicial = this.linha; // Linha onde se inicia a sequ�ncia.
        int colunaInicial = this.coluna; // Coluna onde se inicia a sequ�ncia.

        lexema = lexema + ch; // Cria o lexema apartir da composi��o do caractere lido. 
        this.coluna++;

        Token token = new Token(linhaInicial + 1, colunaInicial + 1, "Delimitador", lexema);
        this.tokens.add(token);
    }
	
	
	/**
	 * Trata a exist�ncia de um caractere no c�digo fonte,
	 * este caractere deve ser delimitado por aspas simples ('),
	 * respeitando a seguinte condi��o: 'letra | d�gito'. 
	 * Casso exista mais de um caractere delimitado por aspas simples,
	 * � considerado um erro l�xico.
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
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
        	//lexema = lexema + ch;
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
           
        	this.addErro("Caractere Inv�lido", lexema, linhaInicial, colunaInicial);
        }
	}
	
	/**
	 * 
	 * 
	 * @param lexema - Token a ser formado
	 * @param ch - Caractere inicial para compor o lexema
	 */
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
        
        else if (ch == '-' && !(numeroAntes)) {
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
        
        else if (ch == '-' && numeroAntes) {
        	//ch = this.leCaractere();
        	//lexema = lexema + ch;
            //this.coluna++;
        }
        
        Token token;
       	if (aritimetico)
       		token = new Token(linhaInicial + 1, colunaInicial + 1, "Operador Aritm�tico", lexema);
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
		
		lexema = lexema + ch;  // Cria o lexema apartir da composi��o do caractere lido. 
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
        	Token token = new Token(linhaInicial + 1, colunaInicial + 1, "D�gito", lexema);
        	tokens.add(token);
        }
        else
        	addErro("Digito Inv�lido", lexema, linhaInicial, colunaInicial);
	}
	
	//Pode ser um identificador, uma palavra reservada ou um operador l�gico
	/**
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
		//a� vai percorrer at� terminar a palavra
		while (!(ch == EOF || Character.isSpaceChar(ch) || estruturaLexica.isDelimitador(ch) || estruturaLexica.isOperador(ch) || ch == '\'' || ch == '"' || ch == '{')) {
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
	
	/**
	 * @param tipo - Tipo do erro
	 * @param erro - Descri��o do erro
	 * @param linha - Linha do erro
	 * @param coluna - Coluna do erro
	 */
	private void addErro(String tipo, String erro, int linha, int coluna) {
		erros.add(erro + " -> " + tipo + " | linha: " + (linha + 1) + " | coluna: " + (coluna + 1));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private char leCaractere(){
		if(!codigoFonte.isEmpty()){
			char c[] = codigoFonte.get(linha).toCharArray();
			if(c.length == coluna){ //verifica se a linha � vazia, ent�o quebra a linha
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
	
	/**
	 * Retorna um ArrayList dos tokens existentes no c�digo
	 * 
	 * @return Tokens existentes
	 */
	public ArrayList<Token> getTokens() {
		return tokens;
	}

	/**
	 * Retorna um ArrayList com os erros encontrados no c�digo
	 * 
	 * @return
	 */
	public ArrayList<String> getErros() {
		return erros;
	}
	

}
