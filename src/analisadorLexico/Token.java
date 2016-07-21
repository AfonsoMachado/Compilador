/**
 * 
 */
package analisadorLexico;

/**
 * @author Afonso Machado
 * @author Henderson Chalegre
 *
 */
public class Token {
	
	private final int linha;
	private final int coluna;
	
	/**
	 *  Representa o tipo do Token: Identificador, Palavra Reservada, entre outros
	 */
	private final String tipo;
	private final String lexema;

	/**
	 * 
	 */
	public Token(int linha, int coluna, String tipo, String lexema) {
		this.tipo = tipo;
		this.linha = linha;
		this.coluna = coluna;
		this.lexema = lexema;
	}

	public String getTipo() {
		return tipo;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}
	
	public String getLexema() {
		return lexema;
	}

}
