/**
 * 
 */
package analisadorLexico;

/**
 * @author Afonso Machado
 *
 */
public class Token {
	
	/**
	 * Linha aonde está localizado o Token
	 */
	private final int linha;
	/**
	 * Coluna aonde está localizado o token
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
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @return
	 */
	public int getLinha() {
		return linha;
	}

	/**
	 * @return
	 */
	public int getColuna() {
		return coluna;
	}
	
	/**
	 * @return
	 */
	public String getLexema() {
		return lexema;
	}

}
