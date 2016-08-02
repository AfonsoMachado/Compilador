package analisadorLexico;

/**
 * compreende todas as forma��es de um token encontrado em um c�digo, sendo que cada token encontrado � uma inst�ncia dessa classe, que cont�m datalhes sobre o lexema, tipo, linha e coluna do token.
 * 
 * @author Afonso Machado
 *
 */
public class Token {
	
	/**
	 * Linha aonde est� localizado o Token
	 */
	private final int linha;
	/**
	 * Coluna aonde est� localizado o token
	 */
	private final int coluna;
	/**
	 *  Representa o tipo do Token: Identificador, Palavra Reservada, entre outros
	 */
	private final String tipo;
	/**
	 * Representa o lexema do Token
	 */
	private final String lexema;

	/**
	 * Construtor da classe Token
	 * 
	 * @param linha
	 * @param coluna
	 * @param tipo
	 * @param lexema
	 */
	public Token(int linha, int coluna, String tipo, String lexema) {
		this.tipo = tipo;
		this.linha = linha;
		this.coluna = coluna;
		this.lexema = lexema;
	}

	/**
	 * Retorna o tipo do token
	 * 
	 * @return tipo do token
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Retorna a linha aonde est� localizado o token
	 * 
	 * @return linha do token
	 */
	public int getLinha() {
		return linha;
	}

	/**
	 * Retorna a coluna aonde come�a o token
	 * 
	 * @return coluna do token
	 */
	public int getColuna() {
		return coluna;
	}
	
	/**
	 * Retorna o lexema relacionado ao token
	 * 
	 * @return lexema do token
	 */
	public String getLexema() {
		return lexema;
	}

}
