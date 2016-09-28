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
		
		
		case  "programa": 
			
			pos = pos +1;
			v = verificaFuncao(tokens, pos, 1, false);
			if(v != null ){
				
			pos = v.getPosicao()+1;
			principal(tokens, pos +1);
			}
			
		case "enquanto":
			
			v = enquantoFaca(tokens, pos+1, 1);
		
			
			if(v != null ){
				pos = v.getPosicao() +1;
				
		
			principal(tokens, pos);
			}
			
			
			
			
		case "var":
							
			v = declaracaoVariaveis(tokens, pos+1, 1);
		
			
			if(v != null ){
				pos = v.getPosicao() +1;
				
		
			principal(tokens, pos);
			}
		case "const":	
			
			
			
			v = declaracaoConstante(tokens, pos+1, 1);
			if(v != null ){
		 	pos = v.getPosicao()+1;
			principal(tokens, pos);
			}
			
		case "leia":
			System.out.println(pos + "  " +t.getLexema() );

			pos = pos +1;
			
			v = verificacaoLeia (tokens, pos, 1);	
			if(v != null ){
			 	pos = v.getPosicao()+1;
			principal(tokens, pos +1);
			}
			
		case "funcao":
			pos = pos +1;
			v = verificaFuncao(tokens, pos, 1, false);
			if(v != null ){
				
			pos = v.getPosicao()+1;
			principal(tokens, pos +1);
			}
		}
		
		
		
		
		
			}else{
				
				System.out.println("Fim do arquivo");
			}
			
			
		
		return pos;
	}
	
	
	
	
	
	
	
	
	

private Vasculhador programa(ArrayList<Token> tokens, int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}










private Vasculhador enquantoFaca(ArrayList<Token> tokens, int posicao, int cont) {
	
	if(posicao < tokens.size() ){
		
		Token t = tokens.get(posicao);
		switch (cont){
				
		case 1:
			
			if(verif.ehAbreParentese(t)){
				

				posicao ++;
				cont ++;
				Vasculhador v1 = new Vasculhador();
				v1 = this.enquantoFaca(tokens, posicao, cont);
				posicao = v1.getPosicao();
				cont = v1.getCont();
				
				return v1;
				
			}else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado um '('");
			}
		
		/////////////////////////////////////////////////////////////
			
		case 2 :
			
		{
			Vasculhador v1 = new Vasculhador();
			v1 = expressaoBooleana(tokens, posicao, cont);
			posicao ++;
			cont ++;
			
			v1 = this.enquantoFaca(tokens, posicao, cont);
			posicao = v1.getPosicao();
			cont = v1.getCont();
			
			return v1;
		}
			
			/////////////////////////////////////////////////////////////
		
		
		case 3:
			
			if(verif.ehFechaParentese(t)){
				

				posicao ++;
				cont ++;
				Vasculhador v1 = new Vasculhador();
				v1 = this.enquantoFaca(tokens, posicao, cont);
				posicao = v1.getPosicao();
				cont = v1.getCont();
				
				return v1;
				
			}else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado um ')'");
			}
			/////////////////////////////////////////////////////////////
			
		case 4: 
			
			if (t.getTipo().equals("Palavra Reservada")){
				if(t.getLexema().equals("faca")){
					
					posicao ++;
					cont ++;
					Vasculhador v1 = new Vasculhador();
					v1 = this.enquantoFaca(tokens, posicao, cont);
					posicao = v1.getPosicao();
					cont = v1.getCont();
					
					return v1;
				}
				
				
			}else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado a palavra 'faca'");
			}
			
			/////////////////////////////////////////////////////////////
			
		case 5:	
			
			if (t.getTipo().equals("Palavra Reservada")){
				if(t.getLexema().equals("inicio")){
					
					posicao ++;
					cont ++;
					Vasculhador v1 = new Vasculhador();
					v1 = this.enquantoFaca(tokens, posicao, cont);
					posicao = v1.getPosicao();
					cont = v1.getCont();
					
					return v1;
				}
				
				
			}else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado a palavra 'inicio'");
			}
			
			
			
			/////////////////////////////////////////////////////////////
			
		case 6: 
		{
			Vasculhador v1 = new Vasculhador();
			
			posicao = this.principal(tokens, posicao);
			
			posicao ++;
			cont ++;
			v1 = this.enquantoFaca(tokens, posicao, cont);
			
			posicao = v1.getPosicao();
			cont = v1.getCont();
			
			return v1;
		}
			/////////////////////////////////////////////////////////////
		
		
		case 7:
			
			if (t.getTipo().equals("Palavra Reservada")){
				if(t.getLexema().equals("fim")){
					
					Vasculhador v1 = new Vasculhador();
					posicao ++;
					cont ++;
					
							
					return v1;
				}
				
				
			}else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado a palavra 'fim'");
			}
			
			/////////////////////////////////////////////////////////////
			
		}//fim do switch
		
	}
	
	
		return null;
	}










private Vasculhador expressaoBooleana(ArrayList<Token> tokens, int posicao, int cont) {
	// TODO Auto-generated method stub
	return null;
}










private Vasculhador verificaFuncao(ArrayList<Token> tokens, int posicao, int cont, boolean retorno) {
	
	
	
	if(posicao < tokens.size() ){
		
		Token t = tokens.get(posicao);
		switch (cont){
				
		case 1: 
			if (t.getTipo().equals("Palavra Reservada")){
				if (verif.ehTipo(t.getLexema())){
					retorno = true;
					posicao ++;
					cont ++;
					
					Vasculhador v1 = new Vasculhador();
					v1 = this.verificaFuncao(tokens, posicao, cont, retorno);
					posicao = v1.getPosicao();
					cont = v1.getCont();
					
					return v1;
					
				} else {
					erro( "Erro na linha: " + t.getLinha() +
							" Era esperado : inteiro, cadeia, real, booleano, caractere");
				} 
				
				
			} else {
				
				retorno = false;
				cont ++;
				
				Vasculhador v1 = new Vasculhador();
				v1 = this.verificaFuncao(tokens, posicao, cont, retorno);
				posicao = v1.getPosicao();
				cont = v1.getCont();
				
				return v1;
				
			}
		/////////////////////////////////////////////////////////////
		case 2: 
			
			if (t.getTipo().equals("Identificador")){	
				
				posicao ++;
				cont ++;
				Vasculhador v1 = new Vasculhador();
				v1 = this.verificaFuncao(tokens, posicao, cont, retorno);
				posicao = v1.getPosicao();
				cont = v1.getCont();
				
				return v1;
				
			}	else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado um identificador");
			} 		
		/////////////////////////////////////////////////////////////	
			
		case 3:	
			
			if(verif.ehAbreParentese(t)){
				posicao ++;
				cont ++;
				Vasculhador v1 = new Vasculhador();
				v1 = this.verificaFuncao(tokens, posicao, cont, retorno);
				posicao = v1.getPosicao();
				cont = v1.getCont();
				
				return v1;
				
			} else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado um '('");
			} 	
		//////////////////////////////////////////////////////////////
			
		case 4: 
			
			if(verif.ehFechaParentese(t)){
				posicao ++;
				cont ++;
				Vasculhador v1 = new Vasculhador();
				v1 = this.verificaFuncao(tokens, posicao, cont, retorno);
				posicao = v1.getPosicao();
				cont = v1.getCont();
				
				return v1;
			}else{
				
				Vasculhador v1 = new Vasculhador();
				v1 = ehParametro(tokens, posicao,1);
				posicao = v1.getPosicao();
				
				v1 = this.verificaFuncao(tokens, posicao, cont, retorno);
				posicao = v1.getPosicao();
				cont = v1.getCont();
				return v1;
				
			}
			
		/////////////////////////////////////////////////////////////	
		case 5: 	
			
			if (t.getTipo().equals("Palavra Reservada")){
				if (t.getLexema().equals("inicio")){
					
					posicao ++;
					cont ++;
					Vasculhador v1 = new Vasculhador();
					v1 = this.verificaFuncao(tokens, posicao, cont, retorno);
					posicao = v1.getPosicao();
					cont = v1.getCont();
					
					return v1;
					
					
				}
				
			} else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado a palavra reservada 'inicio'");
			} 	
			
		/////////////////////////////////////////////////////////////
		
		case 6: 
		{
			Vasculhador v1 = new Vasculhador();
			
			posicao = this.principal(tokens, posicao);
			
			posicao ++;
			cont ++;
			v1 = this.verificaFuncao(tokens, posicao, cont, retorno);
			
			posicao = v1.getPosicao();
			cont = v1.getCont();
			
			return v1;
		}
		/////////////////////////////////////////////////////////////
		case 7: 	
			
			if (t.getTipo().equals("Palavra Reservada")){
			
				if (t.getLexema().equals("fim")){
					
					posicao ++;
					cont ++;
					Vasculhador v2 = new Vasculhador();
					v2 = this.verificaFuncao(tokens, posicao, cont, retorno);
					posicao = v2.getPosicao();
					cont = v2.getCont();
					
					return v2;
				
					
				}
				 	
				
			}else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado a palavra reservada 'fim'");
			}
		
		/////////////////////////////////////////////////////////////	
		case 8 : 
			if(verif.ehAbreParentese(t)){
				
				if (retorno){
					
					posicao ++;
					cont = 9;
					Vasculhador v2 = new Vasculhador();
					v2 = this.verificaFuncao(tokens, posicao, cont, retorno);
					posicao = v2.getPosicao();
					cont = v2.getCont();
					
					return v2;
					
				}else {
					posicao ++;
					cont = 10;
					Vasculhador v2 = new Vasculhador();
					v2 = this.verificaFuncao(tokens, posicao, cont, retorno);
					posicao = v2.getPosicao();
					cont = v2.getCont();
					
					return v2;
					
				}
				
			}else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado um '('");
			}
		
		/////////////////////////////////////////////////////////////
			
		case 9: 
			
			
			if(verif.ehID(t) || verif.ehNumeroDigito(t) ){
				
				posicao ++;
				cont = 10;
				Vasculhador v2 = new Vasculhador();
				v2 = this.verificaFuncao(tokens, posicao, cont, retorno);
				posicao = v2.getPosicao();
				cont = v2.getCont();
				
				return v2;
				
						
		} else {
			
			erro( "Erro na linha: " + t.getLinha() +
					" Era esperado um retorno para essa funcao");
		}
			
		/////////////////////////////////////////////////////////////	
			
		case 10: 
			if (verif.ehFechaParentese(t)){
				
				posicao ++;
				cont++;
				Vasculhador v2 = new Vasculhador();
				posicao = v2.getPosicao();
				cont = v2.getCont();
				
				return v2;
				
				
			}else {
				
				erro( "Erro na linha: " + t.getLinha() +
						" Era esperado um ')'");
			}
			
			
		/////////////////////////////////////////////////////////////
			
		}//fim do switch
		
		
	}
		return null;
	}










private Vasculhador ehParametro(ArrayList<Token> tokens, int posicao, int cont) {
		
	
	if(posicao < tokens.size() ){
		
		Token t = tokens.get(posicao);
		switch (cont){
				
		case 1: 
			
			if (t.getTipo().equals("Palavra Reservada")){
				if (verif.ehTipo(t.getLexema())){
					
					posicao ++;
					cont ++;
					
					Vasculhador v1 = new Vasculhador();
					v1 = this.ehParametro(tokens, posicao, cont);
					posicao = v1.getPosicao();
					cont = v1.getCont();
					
					return v1;
					
				}
				
			}
			
			
		///////////////////////////////////////////////////////////	
		case 2:	
			if (t.getTipo().equals("Identificador")){	
				
				posicao ++;
				cont ++;
				Vasculhador v1 = new Vasculhador();
				v1 = this.ehParametro(tokens, posicao, cont);
				posicao = v1.getPosicao();
				cont = v1.getCont();
				
				return v1;
				
			}
		///////////////////////////////////////////////////////////		
		case 3:
			
			if (verif.ehPontoVirgula(t)){
				
				posicao ++;
				cont = 1;
				Vasculhador v1 = new Vasculhador();
				v1 = this.ehParametro(tokens, posicao, cont);
				posicao = v1.getPosicao();
				cont = v1.getCont();
				
				return v1;
				
				
			}else{
				Vasculhador v1 = new Vasculhador();
				v1.setPosicao(posicao);
				cont ++;
				v1.setCont(cont);
				return v1;
				
				
			}
			
			
		///////////////////////////////////////////////////////////	
		} // fim do switch
		
}	
	return null;
}










private Vasculhador declaracaoConstante(ArrayList<Token> tokens, int posicao, int cont) {

		if(posicao < tokens.size() ){
			Token t = tokens.get(posicao);
			
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




	private Vasculhador verificacaoLeia(ArrayList<Token> tokens, int posicao, int cont) {
		
		if(posicao < tokens.size() ){
			Token t = tokens.get(posicao);
						switch (cont){
			case 1: 
				
				if (verif.ehAbreParentese(t)){
					
					Vasculhador v1 = new Vasculhador();
					posicao ++;
					cont ++;
					v1 = this.verificacaoLeia(tokens, posicao, cont);
					
					posicao = v1.getPosicao();
					cont = v1.getCont();
					
					return v1;
					
					
				}
				
			case 2: 
				
				if (verif.ehID(t)){
				Vasculhador v1 = new Vasculhador();
				posicao ++;
				cont ++;
				v1 = this.verificacaoLeia(tokens, posicao, cont);
				
				posicao = v1.getPosicao();
				cont = v1.getCont();
				
				return v1;
			}
				
			case 3: 
				
					
				 if (verif.ehFechaParentese(t)){
						Vasculhador v1 = new Vasculhador();
						posicao ++;
						cont ++;
						
						v1 = this.verificacaoLeia(tokens, posicao, cont);
						
						posicao = v1.getPosicao();
						cont = v1.getCont();
						
						return v1;
					}
				
				 if (verif.ehVirgula(t)){
					Vasculhador v1 = new Vasculhador();
					posicao ++;
					cont= cont -1;
					System.out.println(cont);
					
					v1 = this.verificacaoLeia(tokens, posicao, cont);
					cont++;
					posicao = v1.getPosicao() + 1;
					
					v1.setCont(cont);
					v1.setPosicao(posicao);
					
					
					return v1;
				 
				}
			case 4:	
				
				if (verif.ehPontoVirgula(t)){
			Vasculhador v1 = new Vasculhador();
			posicao ++;
			cont ++;
			
			v1.setCont(cont);
			v1.setPosicao(posicao);
	
			
			return v1;
				}
			}
			
		
		}else {
			System.out.println("erro");
		}
		
		
		return null;
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
					
									
					
					erro("Erro na linha:" + t.getLinha() +
						"    falta palavra reservada: inteiro, cadeia, real, booleano, caractere");
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
							
						
							v1  = this.declaracaoVariaveis(tokens, posicao, cont);
							//posicao = v1.getPosicao() +1;
							cont = v1.getCont();
							v1.setPosicao(posicao);
							
							return v1;
										
						} else{
							
							erro("Erro na linha:" + t.getLinha() +
							"    Erro na formação da Declaracao da matriz" );
							
						}
					
					}else{
						
						erro("Erro na linha:" + t.getLinha() +
						"    Erro na formação da Declaracao da matriz: Era esperado um ')'" );
						
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
				
				erro("Erro na linha:" + t.getLinha() +
				"    Erro na formação da Declaracao da matriz: Era esperado um id" );
				
			}
			
		case 2: 
			
			
			if(verif.ehFechaParentese(t)){
				
				int aux = posicao +1;
				
				if(verif.ehFechaParentese(tokens.get(aux))){
					
					
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
				}else{
					
					erro("Erro na linha:" + t.getLinha() +
							"    Era esperado um ')'" );
				}
				
				
			}else{
				
				erro("Erro na linha:" + t.getLinha() +
						"    Era esperado um ')'" );
						
			}
			
		}
		
		
		return null;
	}










	private void erro(String erro) {
		
		erros.add(erro);
		
		System.out.println( erro );
		
		
	}

}
					
