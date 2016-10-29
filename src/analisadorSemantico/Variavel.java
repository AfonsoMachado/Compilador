package analisadorSemantico;

public class Variavel {
	
	private String tipo;
	private String nome;
	private String valor;
	private String escopo;

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return the escopo
	 */
	public String getEscopo() {
		return escopo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @param escopo the escopo to set
	 */
	public void setEscopo(String escopo) {
		this.escopo = escopo;
	}

	public Variavel() {
		// TODO Auto-generated constructor stub
	}

}
