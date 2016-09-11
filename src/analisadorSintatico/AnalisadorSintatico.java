package analisadorSintatico;

import java.util.ArrayList;

import analisadorLexico.Token;

public class AnalisadorSintatico {
	
	private Token proximo; // Proximo token da lista
	private ArrayList<Token> tokens;    //lista com os tokens recebidos
    private ArrayList<String> erros;    //lista com os erros encontrados na análise.
    private int contTokens = 0;         //contador que aponta para o proximo token da lista
    
    public void analise(ArrayList<Token> tokens) {
        this.tokens = tokens; //recebe os tokens vindos do lexico.
        proximo = proximo();  //recebe o primeiro token da lista
        erros = new ArrayList<>(); //cria a lista de erros
        
        
        analiseVariáveis();
        analiseConstantes();
    }
    
    private void analiseConstantes() {
		
		
	}

	private void analiseVariáveis() {
		
		
	}

	private Token proximo() {
        if (contTokens < tokens.size()) { //verifica se ainda possuem tokens para a analise.
            return tokens.get(contTokens++);
        } else {
            return new Token(0, 0, "EOF", "EOF");  //cria um token de fim de arquivo. 
        }
    }

}
