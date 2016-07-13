/**
 * 
 */
package analisadorLexico;

/**
 * @author Afonso Machado
 *
 */
public class Token {
	
	private final int linha;
	private final int coluna;
	
	/**
	 *  Representa o tipo do Token: Identificador, Palavra Reservada, entre outros
	 */
	private final String tipo;

	/**
	 * 
	 */
	public Token(int linha, int coluna, String tipo) {
		this.tipo = tipo;
		this.linha = linha;
		this.coluna = coluna;
		
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

}
