package analisadorSintatico;

import java.util.ArrayList;

import analisadorLexico.Token;

public class AnalisadorSintatico {
	
	private Token token; // Proximo token da lista
	private ArrayList<Token> tokens;    //lista com os tokens recebidos
    private ArrayList<String> erros;    //lista com os erros encontrados na anï¿½lise.
    private int contTokens = 0;         //contador que aponta para o proximo token da lista
    private boolean erro = false;
    
    public void analise(ArrayList<Token> tokens) {
        this.tokens = tokens; //recebe os tokens vindos do lexico.
        token = proximo();  //recebe o primeiro token da lista
        erros = new ArrayList<>(); //cria a lista de erros
        
        //QUANDO ACHAR ERRO, DESCONSIDERAR TUDO ATï¿½ O PRï¿½XIMO TOKEN DE SINCRONIZAï¿½ï¿½O
       
        declaracaoPrograma();
        
        //escreva();
        //funcao();
        //declaracao_const();
        
        System.out.println(erros);
        //analiseConstantes();
    }
    
    private void erroSintatico(String erro) {
        if (!token.getLexema().equals("EOF")) {
            erros.add(token.getLinha() + " " + erro + "\n"); //gera o erro normalizado e adiciona na lista de erros.
            token = proximo();
        } else {
            erros.add(erro);
            token = proximo();
        }
    }
    
    private void verificaTipo(String esperado) {
        if ((!token.getLexema().equals("EOF")) && token.getTipo().equals(esperado)) { //verifica se o tipo do token atual e o que era esperado
            token = proximo();
        } else {
            erroSintatico("falta " + esperado); //gera o erro se o tipo do token nao e o esperado 
        }
    }
    
    private void terminal(String esperado) {
        if ((!token.getLexema().equals("EOF")) && token.getLexema().equals(esperado)) { //verifica se o token atual e o que era esperado
            token = proximo();
        } else {
        	System.out.println("ERRO PORRA");
        	erroSintatico("falta " + esperado);   //gera o erro se o token nao e o esperado 
        }
    }
    
    private void declaracao_const() {
    	constx();
    	//<declaracao_const>::=<CONSTX>
        //<CONSTX> ::=<DEC_CONST><CONSTX>|<DEC_CONST>             
        //<DEC_CONST>::='const' <tipo> <id> '=' <valor>';'
		
	}
    
    private void programa() {
    	
    }
    
    private void declaracaoPrograma() {
    	switch (token.getLexema()) {
		case "programa":
			token = proximo();
			tipo();
			identificador("Identificador");
			terminal("(");
			//PARAMETRO PROGRAMA
			terminal(")");
			terminal("inicio");
			//bloco de código
			terminal("fim");
			terminal("(");
			if (!(token.getLexema().equals(")")))
				retornoFuncao();
			terminal(")");
			terminal(";");
			break;
		default:
			erroSintatico("Esperava uma declaracao de programa");
			break;
		}
    }
    
    private void constx() {
    	switch (token.getLexema()) {
		case "const":
			dec_const();
			if(token.getLexema() != ","){
				break;
			} else
				constx();
			break;
		default:
			erroSintatico("Esperava uma declaracao de constante");
			break;
		}
    }
    
    private void dec_const() {
    	switch (token.getLexema()) {
		case "const":
			terminal("const");
			tipo();
			identificador("Identificador");
			terminal("=");
			//falta o valor
			terminal(";");
			System.out.println("terminou");
			break;
		default:
			erroSintatico("Esperava um bloco de contantes");
			break;
		}
    }

	private void tipo() {
		switch (token.getLexema()) {
		case "inteiro":
			terminal("inteiro");
			break;
		case "cadeia":
			terminal("cadeia");
			break;
		case "real":
			terminal("real");
			break;
		case "booleano":
			terminal("booleano");
			break;
		case "caractere":
			terminal("caractere");
			break;
		default:
			erroSintatico("falta palavra reservada: inteiro, cadeia, real, booleano, caractere");
			break;
		}
		
	}

	private void identificador(String esperado) {
		if (!token.getLexema().equals("EOF") && token.getTipo().equals(esperado)) { //verifica se o tipo do token atual e o que era esperado
            token = proximo();
        } else {
            erroSintatico("falta " + esperado); //gera o erro se o tipo do token nao e o esperado 
        }
		
	}

	private void declaracaoVariaveis() {
		switch (token.getLexema()) {
		case "var":
			token = proximo();
			tipo();
			identificador("Identificador");
			if (token.getLexema().equals("(")) {
				declaracaoMatriz();
			}
			listaVariavel();
			break;
		default:
			erroSintatico("Esperava uma declaracao de variavel");
			break;
		}
		//<declaracao_var>   ::=<DECX>
	    //      <DECX>       ::=<DEC><DECX>|<DEC>
		
	}
	
	private void listaVariavel() {
		switch (token.getLexema()) {
		case ",":
			terminal(",");
			identificador("Identificador");
			if (token.getLexema().equals("(")) {
				declaracaoMatriz();
			}
			listaVariavel();
			break;
		case ";":
			terminal(";");
			break;
		default:
			erroSintatico("Esperava um delimitador");
			break;
		}
	}
	
	private void declaracaoMatriz() {
		switch (token.getLexema()) {
		case "(":
			token = proximo();
			terminal("(");
			verificaTipo("Numero");
			terminal(")");
			terminal(")");
			if(token.getLexema().equals("("))
				declaracaoMatriz();
			break;
		default:
			erroSintatico("Esperava um abre parenteses");
			break;
		}
	}
	
	private void funcao() {
		switch (token.getLexema()) {
		case "funcao":
			token = proximo();
			tipo();
			identificador("Identificador");
			terminal("(");
			if(token.getTipo().equals("Palavra Reservada"))
				parametroFuncao();
			terminal(")");
			terminal("inicio");
			
			while(!(token.getLexema().equals("fim"))){
				blocoDeCodigo();
			}
			
			//BLOCO DE CODIGO
			terminal("fim");
			terminal("(");
			if (!(token.getLexema().equals(")")))
				retornoFuncao();
			terminal(")");
			break;

		default:
			erroSintatico("Esperava um bloco de funcao");
			break;
		}
	}
	
	private void escreva() {
		switch (token.getLexema()) {
		case "escreva":
			token = proximo();
			terminal("(");
			retornoFuncao();
			terminal(")");
			terminal(";");
			break;

		default:
			erroSintatico("Esperava a funcao escreva");
			break;
		}
	}
	
	//BX
	private void blocoDeCodigo() {
		switch (token.getLexema()) {
		case "leia":
			
			break;
		case "se":
			
		case "enquanto":
			
		case "escreva":
			escreva();
			
		case "var":
			declaracaoVariaveis();
			
		//case atribuição
		default:
			erroSintatico("Esperava um bloco de código");
			break;
		}
		
	}
	
	private void expressaoBooleana() {
		
	}
	
	private void expressoaAritmetica() {
		
	}
	//OPERADORES REFERENTES À EXPRESSÃO BOOLEANA
	
	private void retornoFuncao() {
		if(token.getTipo().equals("Identificador") || token.getTipo().equals("Numero") || token.getTipo().equals("Digito") || token.getTipo().equals("Caractere") || token.getTipo().equals("Cadeia de caracteres")){
			token = proximo();
		} else {
			erroSintatico("Retorno Incorreto");
			//EXPRESSAO BOOLEANA
		}
	}
	
	
	private void parametroFuncao() {
		tipo();
		identificador("Identificador");
		listaParametros();
	}

	private void listaParametros() {
		switch (token.getLexema()) {
		case ",":
			token = proximo();
			parametroFuncao();
			break;
		default:
			break;
		}
	}
	
	
	

	private Token proximo() {
        if (contTokens < tokens.size()) { //verifica se ainda possuem tokens para a analise.
            return tokens.get(contTokens++);
        } else {
            return new Token(0, 0, "EOF", "EOF");  //cria um token de fim de arquivo. 
        }
    }

}
