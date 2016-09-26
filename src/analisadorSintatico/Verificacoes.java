package analisadorSintatico;

import analisadorLexico.Token;

public class Verificacoes {

	
	public boolean ehID(Token token){
		
		if(token.getTipo().equals("Identificador")){return true;}
		return false;
	}
	
	public boolean ehDelimitador (Token token){
		
		if(token.getTipo().equals("Delimitador")){return true;}
		return false;
		
	}
	
	public boolean ehPontoVirgula(Token token){
		
		if (ehDelimitador(token)){
		if (token.getLexema().equals(";")){	
		}
		return true;
		}
		return false;
	}
	
	
	public boolean ehVirgula(Token token){
		
		if (ehDelimitador(token)){
		if (token.getLexema().equals(",")){	
		}
		return true;
		}
		return false;
	}
	
	
	public boolean ehAbreParentese(Token token){
		
		if (ehDelimitador(token)){
		if (token.getLexema().equals("(")){	
		}
		return true;
		}
		return false;
	}
	
	public boolean ehFechaParentese(Token token){
		
		if (ehDelimitador(token)){
		if (token.getLexema().equals(")")){	
		}
		return true;
		}
		return false;
	}
	
	
	public boolean ehTipo(String lexema) {
		
	
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

	public boolean ehNumeroDigito(Token t) {
		
		if(t.getTipo().equals("Dígito")){
			return true;
			
		}
		
		if (t.getTipo().equals("Numero")){
			
			return true;
		}
		
		
		return false;
	}


}
