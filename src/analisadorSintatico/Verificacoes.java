package analisadorSintatico;

public class Verificacoes {

	
	
	public boolean tipo(String lexema) {
		
	
		switch (lexema) {
		case "inteiro":
			
			return true;
			
		case "cadeia":
			return true;
			
		case "real":
			return true;
			
		case "booleano":
			
			return true;
		case "caractere":
			return true;
			
		default:
			
			return false;
		}
	}


}
