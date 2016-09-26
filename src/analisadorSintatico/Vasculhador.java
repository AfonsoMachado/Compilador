package analisadorSintatico;

public class Vasculhador {
	
	
	private int posicao, cont;
	private boolean matriz = false; 

	public Vasculhador() {
		posicao = 0;
		cont = 0;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public int getCont() {
		return cont;
	}

	public void setCont(int cont) {
		this.cont = cont;
	}

	public void setMatriz(boolean b) {

		matriz = b;
		
	}
	
	public boolean getMatriz(){
		
		return matriz;
	}

}
