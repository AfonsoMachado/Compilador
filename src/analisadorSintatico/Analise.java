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
			
			 pos = v.getPosicao()+1;
			principal(tokens, pos);
			
		case "constante":	
			
			v = declaracaoConstante(tokens, pos+1, 1);
			pos = v.getPosicao()+1;
			principal(tokens, pos);
			 
		case "leia": pos = verificacaoLeia (tokens, pos++, 1);	
		
				principal(tokens, pos +1);
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
			
			
			
			}
			
		}else{
			
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
		
		case 1: if (t.getTipo().equals("Palavra Reservada")){
			
					if (verif.tipo(t.getLexema())){
						
						Vasculhador v1 = new Vasculhador();
						posicao ++;
						cont ++;
						v1 = declaracaoVariaveis(tokens, posicao, cont);					
						
						
						posicao = v1.getPosicao();
						cont = v1.getCont();
						return v1;
					 
					}else{
						
						 erro("falta palavra reservada: inteiro, cadeia, real, booleano, caractere");
					}
		
				}					
					
		
		case 2: 
			
			if (t.getTipo().equals("Identificador")){
			
			
					
			
				Vasculhador v1 = new Vasculhador();
				
				posicao ++;
				cont ++;
				
				v1 = declaracaoVariaveis(tokens, posicao, cont);	
	
				posicao = v1.getPosicao();
				cont = v1.getCont();
				return v1;
			 
				}else{
	
					erro(posicao + "falta de identificador" );
				}
		
		case 3 :			
			
			
			if (t.getTipo().equals("Delimitador")){
				if (t.getLexema().equals(";")){	
					Vasculhador v1 = new Vasculhador();
					v1.setCont(cont++);
					v1.setPosicao(posicao ++);
					return v1;
				}
				if (t.getLexema().equals(",")){	
					Vasculhador v1 = new Vasculhador();
					cont = cont -1;
					posicao ++;
					
					
					return declaracaoVariaveis(tokens, posicao, cont);
					
				}	
				
				
				
				
			}
			
			
			
			
		}
		
		}else{
			
			System.out.println("erro");
		}
		return null;
	
		
		
		
	}



	private void erro(String erro) {
		
		erros.add(erro);
		
		System.out.println("Erro Karai!   " + erro );
		
		
	}

}
