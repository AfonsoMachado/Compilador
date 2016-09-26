private Vasculhador declaracaoConstante(ArrayList<Token> tokens, int posicao, int cont) {
		if(posicao < tokens.size() ){
			Token t = tokens.get(posicao);
			System.out.println();
			switch (cont){
			case 1: 
				if (t.getTipo().equals("Palavra Reservada")){
					if (verif.tipo(t.getLexema())){
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
