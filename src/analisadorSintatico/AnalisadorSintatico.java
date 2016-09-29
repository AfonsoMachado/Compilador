package analisadorSintatico;

import java.util.ArrayList;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

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
        programa();
        //declaracaoPrograma();
        
       // enquanto();
        //escreva();
        //funcao();
        //declaracao_const();
        
        System.out.println(erros);
        //analiseConstantes();
    }
    
    private void erroSintatico(String erro) {
        if (!token.getLexema().equals("EOF")) {
            erros.add(token.getLinha() + " " + erro + "\n"); //gera o erro normalizado e adiciona na lista de erros.
            //token = proximo();
            this.erro = true;
        } else {
        	this.erro = true;
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
    	varGlobal();
    	declaracaoPrograma();
    	Funcoes();
    	
    }
    
    private void Funcoes() {
    	switch (token.getLexema()) {
		case "funcao":
			funcao();
			if(!(token.getLexema().equals("EOF")))
				Funcoes();
			break;
		default:
			erroSintatico("Esperava um bloco de funcoes");
			break;
		}
		
	}

	private void varGlobal() {
    	switch (token.getLexema()) {
		case "const":
			declaracao_const();
			varGlobal();
			break;
			
		case "var":
			declaracaoVariaveis();
			varGlobal();
			break;
		default:
			//token = proximo();
			break;
		}
    }
    
    private void declaracaoPrograma() {
    	switch (token.getLexema()) {
		case "programa":
			token = proximo();
			tipo();
			identificador("Identificador");
			terminal("(");
			if(token.getTipo().equals("Palavra Reservada"))
				parametroPrograma();
			terminal(")");
			terminal("inicio");
			while (!(token.getLexema().equals("fim")) && !(token.getLexema().equals("EOF")))
				blocoDeCodigo();
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
    
    private void enquanto() {
    	switch (token.getLexema()) {
		case "enquanto":
			terminal("enquanto");
			terminal("(");
			expressaoBooleana();
			terminal(")");
			terminal("faca");
			terminal("inicio");
			while(!(token.getLexema().equals("fim")) && !(token.getLexema().equals("EOF"))){
				blocoDeCodigo();
			}
			terminal("fim");
			break;

		default:
			break;
		}
    }
    
    private void leia() {
    	switch (token.getLexema()) {
		case "leia":
			token = proximo();
			terminal("(");
			//identificador("Identificador");
			exp();
			terminal(")");
			terminal(";");
			break;

		default:
			erroSintatico("Experava um leia");
			break;
		}
    }
    
    private void exp(){
    	identificador("Identificador");
    	aux_valor4();
    	exp2();
    }
    
    private void exp2(){
    	switch (token.getLexema()) {
		case ",":
			terminal(",");
			exp();
			break;
		case ")":
			break;
		default:
			erroSintatico("Esperava um conjunto de parametros");
			break;
		}
    }
    
    private void aux_valor1() {
    	switch (token.getLexema()) {
		case "(":
			terminal("(");
			aux_valor2();
			
			break;

		default:
			break;
		}
    }
    
    private void aux_valor2() {
		switch (token.getLexema()) {
		case "(":
			terminal("(");
			aux_valor3();
			break;

		default:
			parametro();
			terminal(")");
			break;
		}
		
	}

	private void parametro() {
		switch (token.getTipo()) {
		case "Cadeia de caracteres":
			token = proximo();
			break;
		case "Caractere":
			token = proximo();
			break;
		default:
			expressaoBooleana();
			break;
		}
		// TODO Auto-generated method stub
		
	}
	
	private void Rparametro() {
		switch (token.getLexema()) {
		case ",":
			terminal(",");
			parametro();
			break;
		default:
			break;
		}
	}

	private void aux_valor3() {
		valor();
		terminal(")");
		terminal(")");
		aux_valor4();
		// TODO Auto-generated method stub
		
	}

	private void aux_valor4() {
    	switch (token.getLexema()) {
		case "(":
			terminal("(");
	    	terminal("(");
	    	valor();
	    	terminal(")");
	    	terminal(")");
	    	aux_valor4();
			break;
		default:
			break;
		}
    }
   
    
    /*private void expressoesLeia() {
    	switch (token.getLexema()) {
		case ",":
			terminal(",");
			identificador("Identificador");
			expressoesLeia();
			break;
		case "(":
			terminal("(");
			terminal("(");
			//VALOR
			terminal(")");
			terminal(")");
			expressoesLeia();
			break;
		case ")":
			break;
		default:
			erroSintatico("Esperava um conjunto de parametros");
			break;
		}
    }*/
    
    private void parametroPrograma() {
			tipo();
			identificador("Identificador");
			listaParametroPrograma();
    }
    
    private void listaParametroPrograma() {
    	switch (token.getLexema()) {
		case ",":
			token = proximo();
			parametroPrograma();
			break;
		default:
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
			valor();
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
			sincroniza();
			listaVariavel();
			break;
		}
	}
	
	private void sincroniza() {
		while(!(token.getLexema().equals(";"))){
			token = proximo();
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
			
			while(!(token.getLexema().equals("fim")) && !(token.getLexema().equals("EOF"))){
				blocoDeCodigo();
			}
			
			terminal("fim");
			terminal("(");
			if (!(token.getLexema().equals(")")))
				retornoFuncao();
			terminal(")");
			terminal(";");
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
		switch (token.getTipo()) {
		
		case "Palavra Reservada":
			blocoPalavraReservada();
			break;
			
		case "Identificador":
			identificador("Identificador");
			Attr();
			break;
		default:
			erroSintatico("Esperava um bloco de código");
			break;
		}
		
	}
	
	private void chamadaFuncao() {
		// TODO Auto-generated method stub
		
	}

	private void Attr() {
		//identificador("Identificador");
		aux_valor1();
		Attr1();
		// TODO Auto-generated method stub
		
	}

	private void Attr1() {
		switch (token.getLexema()) {
		case "=":
			terminal("=");
			Attr2();
			break;
		case ";":
			terminal(";");
		break;
		default:
			break;
		}
		
	}
	

	private void Attr2() {
		switch (token.getTipo()) {
		case "Cadeia de caracteres":
			terminal(token.getLexema());
			terminal(";");
			break;
		case "Caractere":
			terminal(token.getLexema());
			terminal(";");
			break;
		default:
			expressaoAritmetica();
			terminal(";");
			break;
		}
		// TODO Auto-generated method stub
		
	}

	private void blocoPalavraReservada() {
		switch (token.getLexema()) {
			
		case "leia":
			leia();
			break;
			
		case "se":
			
		case "enquanto":
			enquanto();
			break;
		case "escreva":
			escreva();
			break;
			
		case "var":
			declaracaoVariaveis();
			break;
		default:
			erroSintatico("Esperava um bloco de código");
			break;
		}
		
	}

	private void expressaoBooleana() {
		auxExpression();
		expressaoBooleanaR();
	}
	
	private void auxExpression() {
		naoExpressaoAritmetica();
		auxExpressionR();
	}
	
	private void auxExpressionR() {
		switch (token.getLexema()) {
		case ">":
			auxExpression();
			break;
		case "<":
			auxExpression();
			break;
		case "<=":
			auxExpression();
			break;
		case ">=":
			auxExpression();
			break;
		case "=":
			auxExpression();
			break;
		case "<>":
			auxExpression();
			break;
		default:
			break;
		}
		
	}

	private void expressaoBooleanaR() {
		switch (token.getLexema()) {
		case "e":
			terminal("e");
			expressaoBooleana();
			break;
		case "ou":
			terminal("ou");
			expressaoBooleana();
			break;
		default:
			break;
		}
	}
	
	private void naoExpressaoAritmetica() {
		switch (token.getLexema()) {
		case "nao":
			terminal("nao");
			expressaoAritmetica();
			break;

		default:
			expressaoAritmetica();
			break;
		}
		
	}
	
	
	private void expressaoAritmetica() {
		multExp();
		expressaoAritmeticaR();
	}
	
	private void multExp() {
		negExp();
		multExpR();
		
	}
	
	private void multExpR() {
		switch (token.getLexema()) {
		case "*":
			terminal("*");
			multExp();
			break;
		case "/":
			terminal("/");
			multExp();
			break;
		default:
			break;
		}
		
	}

	private void negExp() {
		switch (token.getLexema()) {
		case "-":
			terminal("-");
			valor();
			break;

		default:
			valor();
			break;
		}
		
	}

	private void expressaoAritmeticaR() {
		switch (token.getLexema()) {
		case "+":
			terminal("+");
			expressaoAritmetica();
			break;
		case "-":
			terminal("-");
			expressaoAritmetica();
		default:
			break;
		}
	}
	
	private void valor() {
		switch (token.getTipo()) {
		case "Palavra Reservada":
			if(token.getLexema().equals("verdadeiro"))
				terminal("verdadeiro");
			else if(token.getLexema().equals("falso"))
				terminal("falso");
			break;
		case "Identificador":
			identificador("Identificador");
			aux_valor1();
			break;
		case "Numero":
			verificaTipo("Numero");
			break;
		case "Digito":
			verificaTipo("Digito");
			break;
		default:
			terminal("(");
			expressaoBooleana();
			terminal(")");
			break;
		}
	}
	//OPERADORES REFERENTES À EXPRESSÃO BOOLEANA
	
	private void retornoFuncao() {
		if(token.getTipo().equals("Identificador") || token.getTipo().equals("Numero") || token.getTipo().equals("Digito") || token.getTipo().equals("Caractere") || token.getTipo().equals("Cadeia de caracteres")){
			token = proximo();
		} else {
			erroSintatico("Retorno Incorreto");
			//token = proximo();
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
