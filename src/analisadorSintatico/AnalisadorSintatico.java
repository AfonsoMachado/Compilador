package analisadorSintatico;

import java.util.ArrayList;

import analisadorLexico.Token;

public class AnalisadorSintatico {
	
	private Token token; // Proximo token da lista
	private ArrayList<Token> tokens;    //lista com os tokens recebidos
    private ArrayList<String> erros;    //lista com os erros encontrados na an�lise.
    private int contTokens = 0;         //contador que aponta para o proximo token da lista
    private boolean erro = false;
    
    public void analise(ArrayList<Token> tokens) {
        this.tokens = tokens; //recebe os tokens vindos do lexico.
        token = proximo();  //recebe o primeiro token da lista
        erros = new ArrayList<>(); //cria a lista de erros
        
        //QUANDO ACHAR ERRO, DESCONSIDERAR TUDO AT� O PR�XIMO TOKEN DE SINCRONIZA��O
        
        declaracao_const();
        
        System.out.println(erros);
        //analiseConstantes();
    }
    
    private void erroSintatico(String erro) {
        if (!token.getLexema().equals("EOF")) {
            erros.add(token.getLinha() + " " + erro + "\n"); //gera o erro normalizado e adiciona na lista de erros.
        } else {
            erros.add(erro);
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
            //token = proximo();
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

	private void analiseVariaveis() {
		//<declaracao_var>   ::=<DECX>
	    //      <DECX>       ::=<DEC><DECX>|<DEC>
		
	}
	

	private Token proximo() {
        if (contTokens < tokens.size()) { //verifica se ainda possuem tokens para a analise.
            return tokens.get(contTokens++);
        } else {
            return new Token(0, 0, "EOF", "EOF");  //cria um token de fim de arquivo. 
        }
    }

}
