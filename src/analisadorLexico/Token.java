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
	
	/**
	 *  Representa o tipo do Token: Identificador, Palavra Reservada, entre outros
	 */
	private final String tipo;

	/**
	 * 
	 */
	public Token(int linha, String tipo) {
		this.tipo = tipo;
		this.linha = linha;
		
	}

	public String getTipo() {
		return tipo;
	}

	public int getLinha() {
		return linha;
	}

}
