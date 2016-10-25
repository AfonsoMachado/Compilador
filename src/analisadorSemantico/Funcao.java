package analisadorSemantico;

public class Funcao {
	
	//private final String retorno;
	private final String nome;
	private final String tipoRetorno;

	public Funcao(String tipoRetorno, String nome) {
		//this.retorno = retorno;
		this.nome = nome;
		this.tipoRetorno = tipoRetorno;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return the tipoRetorno
	 */
	public String getTipoRetorno() {
		return tipoRetorno;
	}
	

}
