package analisadorSintatico;

import java.util.ArrayList;

import analisadorLexico.Token;

public class AnalisadorSintatico {
	
	private Token token; // Proximo token da lista
	private ArrayList<Token> tokens;    //lista com os tokens recebidos
    private ArrayList<String> erros;    //lista com os erros encontrados na análise.
    private int contTokens = 0;         //contador que aponta para o proximo token da lista
    
    public void analise(ArrayList<Token> tokens) {
        this.tokens = tokens; //recebe os tokens vindos do lexico.
        token = proximo();  //recebe o primeiro token da lista
        erros = new ArrayList<>(); //cria a lista de erros
        
        
        analiseVariáveis();
        //analiseConstantes();
    }
    
    private void terminal(String esperado) {
        if ((!token.getLexema().equals("EOF")) && token.getLexema().equals(esperado)) { //verifica se o token atual e o que era esperado
            token = proximo();
        } else {
        	System.out.println("ERRO PORRA");
            //erroSintatico("falta " + esperado); //gera o erro se o token nao e o esperado 
        }
    }
    
    private void declaracao_const() {
    	constx();
    	//<declaracao_const>::=<CONSTX>
        //<CONSTX> ::=<DEC_CONST><CONSTX>|<DEC_CONST>             
        //<DEC_CONST>::='const' <tipo> <id> '=' <valor>';'
		
	}
    
    private void constx() {
    	dec_const();
    }
    
    private void dec_const() {
    	switch (token.getLexema()) {
		case "const":
			
			break;

		default:
			break;
		}
    }

	private void analiseVariáveis() {
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
