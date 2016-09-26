package analisadorSintatico;

import java.util.ArrayList;

import analisadorLexico.Token;

public class Analise {
	
		
		private Verificacoes verif = new Verificacoes();
		private Vasculhador v = new Vasculhador();
		private ArrayList<String> erros = new ArrayList<String>();
	
	
	public int principal(ArrayList<Token> tokens, int pos) {
		
	
			if (pos < tokens.size()){
		
		v.setPosicao(pos);
		Token t = tokens.get(pos);
		
		
		switch (t.getLexema()){
		
		
		case "var":
							
			v = declaracaoVariaveis(tokens, pos+1, 1);
		
			System.out.println("hahahaha " + v.getPosicao());
			if(v != null ){
				pos = v.getPosicao() +1;
				System.out.println("j " + pos);
		
			principal(tokens, pos);
			}
		case "const":	
			
			//System.out.println("rr " + pos);
			
			v = declaracaoConstante(tokens, pos+1, 1);
		//	pos = v.getPosicao()+1;
			//principal(tokens, pos);
			 
		case "leia": pos = verificacaoLeia (tokens, pos++, 1);	
		
			//	principal(tokens, pos +1);
		}
		
		
		
		
			}else{
				
				System.out.println("Fim do arquivo");
			}
			
			
		
		return pos;
	}
	
	
	
	
	
	
	
	
	
	
	private Vasculhador declaracaoConstante(ArrayList<Token> tokens, int posicao, int cont) {
		if(posicao < tokens.size() ){
			Token t = tokens.get(posicao);
			System.out.println();
			switch (cont){
			case 1: 
				if (t.getTipo().equals("Palavra Reservada")){
					if (verif.ehTipo(t.getLexema())){
						Vasculhador v1 = new Vasculhador();
						posicao ++;
						cont ++;
						v1 = declaracaoConstante(tokens, posicao, cont);					
						posicao = v1.getPosicao();
						cont = v1.getCont();
						return v1;
					} else {
						erro("falta palavra reservada: inteiro, cadeia, real, booleano, caractere");
					}
				} else {
					erro("falta palavra reservada: inteiro, cadeia, real, booleano, caractere");
				}
			case 2:
				if (t.getTipo().equals("Identificador")){
					Vasculhador v1 = new Vasculhador();
					posicao ++;
					cont ++;
					v1 = declaracaoConstante(tokens, posicao, cont);	
					posicao = v1.getPosicao();
					cont = v1.getCont();
					return v1;
				} else {
					erro(posicao + "falta de identificador" );
				}
			case 3:
				if (t.getTipo().equals("Operador Relacional")){
					if (t.getLexema().equals("=")){
						Vasculhador v1 = new Vasculhador();
						posicao ++;
						cont ++;
						v1 = declaracaoConstante(tokens, posicao, cont);	
						posicao = v1.getPosicao();
						cont = v1.getCont();
						return v1;
					} else {
						erro("Operador relacional incorreto");
					}
				} else {
					erro(posicao + "falta de operador relacional");
				}
			case 4: //VALOR
				
			case 5:
				if (t.getTipo().equals("Delimitador")){
					if (t.getLexema().equals(";")){	
						Vasculhador v1 = new Vasculhador();
						v1.setCont(cont++);
						v1.setPosicao(posicao ++);
						return v1;
					} else {
						erro("Delimitador incorreto");
					}
				} else {
					erro("Inexistencia de um delimitador");
				}
			} // fim do switch case
		} else {
			System.out.println("erro");
		}
		return null;
}




	private int verificacaoLeia(ArrayList<Token> tokens, int posicao, int cont) {
		
		
		return posicao;
	}



	private Vasculhador declaracaoVariaveis(ArrayList<Token> tokens, int posicao, int cont){
		if(posicao < tokens.size() ){
			Token t = tokens.get(posicao);
			System.out.println();
			switch (cont){
			case 1: 
				if (t.getTipo().equals("Palavra Reservada")){
					if (verif.ehTipo(t.getLexema())){
						Vasculhador v1 = new Vasculhador();
						posicao ++;
						cont ++;
						v1 = declaracaoVariaveis(tokens, posicao, cont);
						System.out.println(v1.getPosicao());
						if(v1 != null){
						posicao = v1.getPosicao();
						
						System.out.println("e"+posicao);
						cont = v1.getCont();}
						return v1;
					} else {
						erro("falta palavra reservada: inteiro, cadeia, real, booleano, caractere");
					}

				} else {
					erro("falta palavra reservada: inteiro, cadeia, real, booleano, caractere");
				}
			case 2: 
				if (t.getTipo().equals("Identificador")){
					Vasculhador v1 = new Vasculhador();
					posicao ++;
					cont ++;
					v1 = declaracaoVariaveis(tokens, posicao, cont);	
					System.out.println("ee"+posicao);
					System.out.println("ee4  " + v1.getPosicao());
					if(v1 != null){
					posicao = v1.getPosicao();
					
					cont = v1.getCont();
					System.out.println("ee"+posicao);
					}
					return v1;
				} else {
					erro(posicao + "falta de identificador" );
				}
			case 3 :	
				
				if (t.getTipo().equals("Delimitador")){
					if (t.getLexema().equals(";")){	
						Vasculhador v1 = new Vasculhador();
						
						
						v1.setCont(cont++);
						v1.setPosicao(posicao++);
						System.out.println("dw" + v1.getPosicao());
						System.out.println("d" + posicao);
						return v1;
					}
					if (t.getLexema().equals(",")){	
						Vasculhador v1 = new Vasculhador();
						cont = cont -1;
						posicao ++;
						v1 = declaracaoVariaveis(tokens, posicao, cont);
						posicao = v1.getPosicao();
						cont = v1.getCont();
						
						return v1;
					}
					
				if (verif.ehAbreParentese(t)){
					
					posicao++;
					t = tokens.get(posicao);
					if (verif.ehAbreParentese(t)){
					System.out.println("aad");
					
					Vasculhador v1 = new Vasculhador();
					posicao++;;
					
					v1 = this.ehmatriz(tokens, posicao, 1);
					
						if(v1.getMatriz()){
							posicao = v1.getPosicao();
							
							System.out.println (v1.getPosicao());
							System.out.println("aaqd");
							v1  = this.declaracaoVariaveis(tokens, posicao, cont);
							//posicao = v1.getPosicao() +1;
							cont = v1.getCont();
							v1.setPosicao(posicao);
							System.out.println(v1.getPosicao() + "!!");
							return v1;
							
							
						}
					
					}
				}	
				}
			}
		} else {
			System.out.println("erro");
		}
		return null;
	}







	



	


	private Vasculhador ehmatriz(ArrayList<Token> tokens, int posicao, int i) {

		
		Token t = tokens.get(posicao);
		
		
		switch(i){
		
		case 1: 
			System.out.println(t.getTipo() + "  " + posicao + " " + t.getLexema() + " " + i );
			
			if(verif.ehID(t) || verif.ehNumeroDigito(t)){ 
				
				 Vasculhador v1 = new Vasculhador();
				 posicao++;
				 i++;
				 v1 = ehmatriz(tokens, posicao, i); 
				 i = v1.getCont() +1;
				 posicao = v1.getPosicao() + 1;
				 
				 v1.setCont(i);
				 v1.setPosicao(posicao);
				 return v1;
			}else{
				
				erro("Erro na formação da Declaracao da matriz: Era esperado um id" );
				
			}
			
		case 2: 
			
			
			if(verif.ehFechaParentese(t)){
				System.out.println(t.getTipo() + "  " + posicao + " " + t.getLexema() );
				
				int aux = posicao +1;
				System.out.println(tokens.get(aux).getLexema());
				if(verif.ehFechaParentese(tokens.get(aux))){
					
					System.out.println(tokens.get(aux).getTipo() + "  " + aux + " " + tokens.get(aux).getLexema() + "  " +i );
					Vasculhador v1 = new Vasculhador();
					System.out.println(posicao);
					posicao = posicao +1;
					System.out.println(posicao);
					i++;
					v1.setCont(i); 
					v1.setPosicao(posicao);
					v1.setMatriz(true);
					
					System.out.println(v1.getPosicao());
					return v1;
				}
				
				
			}
			
		}
		
		
		return null;
	}










	private void erro(String erro) {
		
		erros.add(erro);
		
		System.out.println("Erro Karai!   " + erro );
		
		
	}

}
