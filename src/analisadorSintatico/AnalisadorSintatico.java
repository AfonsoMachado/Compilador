package analisadorSintatico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import analisadorLexico.AnalisadorLexico;
import analisadorLexico.Token;
import analisadorSemantico.Constante;
import analisadorSemantico.Funcao;
import analisadorSemantico.Variavel;

/**
 * @author Afonso Machado
 * @author Henderson Chalegre
 * 
 * @see AnalisadorLexico
 *
 */
public class AnalisadorSintatico {
	
	private ArrayList<Variavel> parametrosFuncaoAux = new ArrayList<>();
	private boolean ehFuncao = false;
	private int auxFuncao = 0;
	
	private String escopoAtual = new String();
	
	private ArrayList<ArrayList<Variavel>> escopos = new ArrayList<ArrayList<Variavel>>();
	private boolean escopoLocal = false;
	private int hierarquiaEscopoAtual = 0;
	
	private ArrayList<Variavel> varAux = new ArrayList<>();
	private ArrayList<Variavel> varEscopoGlobal = new ArrayList<>();
	private ArrayList<Constante> constantes = new ArrayList<>();
	private ArrayList<Funcao> funcoes = new ArrayList<>();
	private ArrayList<Variavel> variaveis = new ArrayList<>();
	
	private String valorAtual;
	private Variavel varAtual;
	private Constante consAtual;
	private ArrayList<String> errosSemanticos = new ArrayList<>();
	
	/**
	 * Proximo token da lista
	 */
	private Token token; // Proximo token da lista
	/**
	 * Lista com os tokens recebidos do analisador lexico
	 */
	private ArrayList<Token> tokens;    //lista com os tokens recebidos
    /**
     * Lista com os erros encontrados na analise sintatica
     */
    private ArrayList<String> erros;    //lista com os erros encontrados na an�lise.
    /**
     * Contador que aponta para o proximo token da lista
     */
    private int contTokens = 0;         //contador que aponta para o proximo token da lista
	private String funcAtual;
    
    /**
     * Metodo que faz a analise por completo a partir do simbolo inicial da gramatica
     * 
     * @param tokens - tokens encontrados pelo analisador lexico
     */
    public void analise(ArrayList<Token> tokens) {
    	
    	for (int i = 0; i < 26; i++) {
    		ArrayList<Variavel> v = new ArrayList<Variavel>();
    		escopos.add(v);
    	}
    	
        this.tokens = tokens; //recebe os tokens vindos do lexico.
        token = proximo();  //recebe o primeiro token da lista
        erros = new ArrayList<>(); //cria a lista de erros
        
        programa(); //invoca o simbolo inicial da gramatica
        
        if(erros.size() != 0) {
        	System.out.println("Ocorreram erros na analise sintatica");
        	System.out.println(erros); //imprime os erros na tela
        }
        else
        	System.out.println("Analise Sintatica feita com sucesso\n");
        
        System.out.println(constantes);
        System.out.println(errosSemanticos);
    }
    
    /**
     * 
     * 
     * @param erro - String indicando um erro disparado pela analise
     */
    private void erroSintatico(String erro) {
        if (!token.getLexema().equals("EOF")) {
            erros.add("Linha: " + (token.getLinha()+1) + " -> " + erro + "\n"); //gera o erro normalizado e adiciona na lista de erros.
            //token = proximo();
        } else {
            erros.add(erro);
            //token = proximo();
        }
    }
    
    /**
     * Verifica se um dado token � do tipo esperado
     * 
     * @param esperado
     * @return 
     */
    private String verificaTipo(String esperado) {
        if ((!token.getLexema().equals("EOF")) && token.getTipo().equals(esperado)) { //verifica se o tipo do token atual e o que era esperado
        	String t = token.getLexema();
        	token = proximo();
            return t;
        } else {
            erroSintatico("falta " + esperado); //gera o erro se o tipo do token nao e o esperado 
            return null;
        }
    }
    
    /**
     * Verifica se � um terminal, caso n�o seja, um erro � disparado
     * 
     * @param esperado
     */
    private void terminal(String esperado) {
        if ((!token.getLexema().equals("EOF")) && token.getLexema().equals(esperado)) { //verifica se o token atual e o que era esperado
            token = proximo();
        } else {
        	erroSintatico("Falta o token: " + esperado);   //gera o erro se o token nao e o esperado 
        }
    }
    
    /**
     * Retorna o proximo token da lista
     * 
     * @return pr�ximo token da lista de tokens
     */
    private Token proximo() {
        if (contTokens < tokens.size()) { //verifica se ainda possuem tokens para a analise.
            return tokens.get(contTokens++);
        } else {
            return new Token(0, 0, "EOF", "EOF");  //cria um token de fim de arquivo. 
        }
    }
	
	/**
	 * @return erros disparados pelo analisador sintatico
	 */
	public ArrayList<String> getErros(){
		return this.erros;
	}
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*INICIO DOS METODOS USADOS PARA REALIZAR AS PRODUCOES DA GRAMATICA, SENDO CADA NAO TERMINAL UM METODO*/
    
    private void declaracao_const() {
    	constx();
		
	}
    
    private void programa() {
    	escopoAtual = "global";
    	varGlobal();
    	/*for(Variavel v: variaveis) {
    		//varEscopoGlobal.add(v);
    		v.setEscopo("global");
    	}*/
    	assinaturaFuncoes();
    	declaracaoPrograma();
    	ArrayList<Variavel> varsAux = new ArrayList<>();
    	for (Variavel v : variaveis) {
			if(v.getEscopo().equals("global"))
				varsAux.add(v);
		}
    	variaveis.clear();
    	for (Variavel v : varsAux) {
				variaveis.add(v);
		}
    	Funcoes();
    	System.out.println(variaveis);
    	
    }
    
    private void assinaturaFuncoes() {
		while (!(token.getLexema().equals("programa"))) {
			Funcao f = new Funcao();
			terminal("funcao");
			if(token.getTipo().equals("Palavra Reservada")) {
				f.setTipoRetorno(token.getLexema());
				tipo();
				f.setNome(token.getLexema());
				identificador("Identificador");
			} else {
				f.setNome(token.getLexema());
				identificador("Identificador");
			}
			//String nome = token.getLexema();
			terminal("(");
			if(token.getTipo().equals("Palavra Reservada"))
				parametroFuncao();
			f.setParametros(parametrosFuncaoAux);
			funcoes.add(f);
			parametrosFuncaoAux = new ArrayList<>();
			terminal(")");
			terminal(";");
			System.err.println("dd");
		}
		
	}

	private void Funcoes() {
		for (Variavel v : variaveis){
    		if(v.getEscopo().equals("global"))
    			varEscopoGlobal.add(v);
    	}
    	variaveis = new ArrayList<>();
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
			escopoAtual = "programa";
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
			//bloco de c�digo
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
			erroSintatico("Esperava um leia");
			break;
		}
    }
    
    private void exp(){
    	switch (token.getTipo()) {
		case "Identificador":
			if(!(escopoAtual.equals("programa")) && !(escopoAtual.equals("global"))){
				boolean existe = false;
				int aux = hierarquiaEscopoAtual;
				for (ArrayList<Variavel> escopo : escopos) {
					
					while(aux > 0) {
						ArrayList<Variavel> vars = escopos.get(aux);
						for (Variavel var : vars) {
							if(token.getLexema().equals(var.getNome()) && (var.getTipo().equals("inteiro") || var.getTipo().equals("real"))){
								existe = true;
								break;
							}
								
						}
						aux--;
					}
				}
				
				for (Variavel vv : variaveis) {
					if(token.getLexema().equals(vv.getNome()) && (vv.getTipo().equals("inteiro") || vv.getTipo().equals("real"))) {
						existe = true;
					} 
				}
				for (Constante constante : constantes) {
					if(token.getLexema().equals(constante.getId()) && (constante.getTipo().equals("inteiro") || constante.getTipo().equals("real"))) {
						existe = true;
					}
				}
				
				if(!existe && token.getTipo().equals("Identificador"))
					errosSemanticos.add("Linha " + token.getLinha() + " -> A Variavel ou constante '" + token.getLexema() + "' nao existe\n");
				token = proximo();
			}
			else {
				boolean existe = false;
				for (Variavel vv : variaveis) {
					if(token.getLexema().equals(vv.getNome()) && (vv.getTipo().equals("inteiro") || vv.getTipo().equals("real"))) {
						existe = true;
					} 
				}
				for (Constante constante : constantes) {
					if(token.getLexema().equals(constante.getId()) && (constante.getTipo().equals("inteiro") || constante.getTipo().equals("real"))) {
						existe = true;
					}
				}
				if(!existe)
					errosSemanticos.add("Linha " + token.getLinha() + " -> A Variavel ou constante '" + token.getLexema() + "' nao existe\n");
				token = proximo();
			}
			identificador("Identificador");
	    	aux_valor4();
	    	exp2();
			break;
		default:
			break;
		}
    }
    
    private void exp2(){
    	switch (token.getLexema()) {
		case ",":
			terminal(",");
			exp();
			break;
		default:
			//erroSintatico("Esperava um conjunto de parametros");
			break;
		}
    }
    
    private void aux_valor1() {
    	switch (token.getLexema()) {
		case "(":
			terminal("(");
			ehFuncao = true;
			aux_valor2();
			
			break;
		case "/":
			terminal("/");
			dxyz();
		default:
			//token = proximo();
			break;
		}
    }
    
    private void dxyz() {
		switch(token.getLexema()) {
		case "*":
			aux_valor5();
			break;
		default:
			expressaoAritmetica();
			break;
		}
		
	}

	private void aux_valor5() {
		terminal("*");
		aux_valor3();
		
	}

	private void aux_valor2() {
			parametro();
			terminal(")");
			for (Funcao ff : funcoes) {
				if (ff.getNome().equals(funcAtual)) {
					if(auxFuncao != ff.getParametros().size()) {
						errosSemanticos.add("Linha " + token.getLinha() + " -> Quantidade incorreta de parametros\n");
					}
						
				}
			}
			funcAtual = null;
			auxFuncao = 0;
			ehFuncao = false;
	}

	private void parametro() {
		Funcao f = new Funcao();
		if(auxFuncao == 0){
			auxFuncao = 0;
		}
		for (Funcao ff : funcoes) {
			if (ff.getNome().equals(funcAtual)) {
				f = ff;
			}
		}
		switch (token.getTipo()) {
		case "Cadeia de caracteres":
			
			if (auxFuncao < f.getParametros().size()) {
				boolean existe1 = false;
				if(f.getParametros().get(auxFuncao).getTipo().equals("cadeia")) {
					existe1 = true;
				}
				if(!existe1)
					errosSemanticos.add("Linha " + token.getLinha() + " -> Atribuicao incorreta de parametro\n");
				
			}
			
			if(token.getLexema().equals(")")){
				ehFuncao = false;
			}	
			
			token = proximo();
			auxFuncao++;
			Rparametro();
			break;
		case "Caractere":
			
			if (auxFuncao < f.getParametros().size()) {
				boolean existe2 = false;
				if(f.getParametros().get(auxFuncao).getTipo().equals("caractere")) {
					existe2 = true;
				}
				if(!existe2)
					errosSemanticos.add("Linha " + token.getLinha() + " -> Atribuicao incorreta de parametro\n");	
			}
			
			if(token.getLexema().equals(")")){
				ehFuncao = false;
			}
			
			token = proximo();
			auxFuncao++;
			Rparametro();
			break;
		case "Identificador":
			
			if (auxFuncao < f.getParametros().size()) {
				if(!(escopoAtual.equals("programa")) && !(escopoAtual.equals("global"))){
					boolean existe = false;
					int aux = hierarquiaEscopoAtual;
					for (ArrayList<Variavel> escopo : escopos) {
						
						while(aux > 0) {
							ArrayList<Variavel> vars = escopos.get(aux);
							for (Variavel var : vars) {
								if(token.getLexema().equals(var.getNome()) && f.getParametros().get(auxFuncao).getTipo().equals(var.getTipo())){
									existe = true;
									break;
								}
									
							}
							aux--;
						}
					}
					
					for (Variavel vv : variaveis) {
						if(token.getLexema().equals(vv.getNome()) && f.getParametros().get(auxFuncao).getTipo().equals(vv.getTipo())) {
							existe = true;
						} 
					}
					for (Constante constante : constantes) {
						if(token.getLexema().equals(constante.getId()) && f.getParametros().get(auxFuncao).getTipo().equals(constante.getTipo())) {
							existe = true;
						}
					}
					
					if(!existe && token.getTipo().equals("Identificador"))
						errosSemanticos.add("Linha " + token.getLinha() + " -> A vvariavel ou constante '" + token.getLexema() + "' nao existe ou tem tipagem incorreta\n");
				}
				else {
					boolean existe = false;
					for (Variavel vv : variaveis) {
						if(token.getLexema().equals(vv.getNome()) && f.getParametros().get(auxFuncao).getTipo().equals(vv.getTipo())) {
							existe = true;
						} 
					}
					for (Constante constante : constantes) {
						if(token.getLexema().equals(constante.getId()) && f.getParametros().get(auxFuncao).getTipo().equals(constante.getTipo())) {
							existe = true;
						}
					}
					if(!existe)
						errosSemanticos.add("Linha " + token.getLinha() + " -> A variavel ou constante '" + token.getLexema() + "' nao existe ou tem tipagem incorreta\n");
				}
			}
			
			if(token.getLexema().equals(")")){
				ehFuncao = false;
			}	
			
			auxFuncao++;
			
			System.err.println("ak");
			identificador("Identificador");
			Rparametro();
			break;
		case "Palavra Reservada":
			
			if (auxFuncao < f.getParametros().size()) {
				if(token.getLexema().equals("verdadeiro"))
					terminal("verdadeiro");
				else if(token.getLexema().equals("falso"))
					terminal("falso");
			}
			
			if(token.getLexema().equals(")")){
				ehFuncao = false;
			}	
			auxFuncao++;
			Rparametro();
			break;
		case "Numero":
			
			if (auxFuncao < f.getParametros().size()) {
				boolean existe3 = false;
				if(f.getParametros().get(auxFuncao).getTipo().equals("real")) {
					existe3 = true;
				}
				else if (f.getParametros().get(auxFuncao).getTipo().equals("real") && token.getLexema().contains(".")) {
					existe3 = true;
				} 
				else if (f.getParametros().get(auxFuncao).getTipo().equals("inteiro") && !token.getLexema().contains(".")) {
					existe3 = true;
				}
				if(!existe3)
					errosSemanticos.add("Linha " + token.getLinha() + " -> Atribuicao incorreta de parametro\n");
				//token = proximo();
			}
			if(token.getLexema().equals(")")){
				ehFuncao = false;
			}
			
			verificaTipo("Numero");
			auxFuncao++;
			Rparametro();
			break;
		case "Digito":
			if (auxFuncao < f.getParametros().size()) {
				boolean existe4 = false;
				if(f.getParametros().get(auxFuncao).getTipo().equals("inteiro")) {
					existe4 = true;
				}
				if(!existe4)
					errosSemanticos.add("Linha " + token.getLinha() + " -> Atribuicao incorreta de parametro\n");
				//token = proximo();
			}
			if(token.getLexema().equals(")")){
				ehFuncao = false;
			}	
			verificaTipo("Digito");
			auxFuncao++;
			Rparametro();
			break;
		case "Delimitador":
			if(token.getLexema().equals("(")){
				terminal("(");
				expressaoBooleana();
				terminal(")");
				Rparametro();
			}
		default:
			auxFuncao = 0;
			break;
		}
		
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
		terminal("*");
		terminal("/");
		aux_valor4();
	}

	private void aux_valor4() {
    	switch (token.getTipo()) {
		case "Operador Aritmetico":
			if(token.getLexema().equals("+") || token.getLexema().equals("-")) {
				expressaoAritmetica();
				break;
			}
			terminal("/");
	    	terminal("*");
	    	valor();
	    	terminal("*");
	    	terminal("/");
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
			Constante c = new Constante();
			terminal("const");
			c.setTipo(token.getLexema());
			tipo();
			c.setId(token.getLexema());
			identificador("Identificador");
			terminal("=");		
			valor();
			c.setC(valorAtual);
			consAtual = c;
			int linha = token.getLinha();
			terminal(";");
			if(!verificaTipoSemantico())
				errosSemanticos.add("Linha " + linha + " -> Atrubicao nao e do tipo " + consAtual.getTipo());
			//verificaTipoSemantico();
			constantes.add(c);
			consAtual = null;
			valorAtual = null;
			varAtual = null;
			break;
		default:
			erroSintatico("Esperava um bloco de contantes");
			break;
		}
    }

	private boolean verificaTipoSemantico() {
		if (valorAtual != null && varAtual != null) {
			if((valorAtual.matches("^[0-9]*[.]{0,1}[0-9]*$") || valorAtual.matches("^[0-9]*$")) && varAtual.getTipo().equals("real"))
				return true;
			else if(valorAtual.matches("^[0-9]*$") && varAtual.getTipo().equals("inteiro"))
				return true;
			else if(valorAtual.matches("^[a-zA-Z��������������������������]*$") && varAtual.getTipo().equals("cadeia"))
				return true;
			else if((valorAtual.equals("verdadeiro") || valorAtual.equals("false")) && varAtual.getTipo().equals("booleano"))
				return true;
		} else if (valorAtual != null && consAtual != null) {
			if((valorAtual.matches("^[0-9]*[.]{0,1}[0-9]*$") || valorAtual.matches("^[0-9]*$")) && consAtual.getTipo().equals("real"))
				return true;
			else if(valorAtual.matches("^[0-9]*$") && consAtual.getTipo().equals("inteiro"))
				return true;
			else if(valorAtual.matches("^[a-zA-Z��������������������������]*$") && consAtual.getTipo().equals("cadeia"))
				return true;
			else if((valorAtual.equals("verdadeiro") || valorAtual.equals("false")) && consAtual.getTipo().equals("booleano"))
				return true;
		}
		return false;
		
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
		boolean aninhado = false;
		boolean errosemantico = false;
		switch (token.getLexema()) {
		case "var":
			token = proximo();
			Variavel v = new Variavel();
			String tipo = token.getLexema(); 
			v.setTipo(tipo);
			tipo();
			v.setNome(token.getLexema());
			v.setEscopo(escopoAtual);
			if(!(escopoAtual.equals("programa")) && !(escopoAtual.equals("global"))){
				//ArrayList<Variavel> vars = escopos.get(hierarquiaEscopoAtual);
				int aux = hierarquiaEscopoAtual;
				for (ArrayList<Variavel> escopo : escopos) {
					
					while(aux > 0) {
						ArrayList<Variavel> vars = escopos.get(aux);
						for (Variavel var : vars) {
							if(var.getNome().equals(token.getLexema())){
								errosemantico = true;
								errosSemanticos.add("Linha "+ token.getLinha() + " -> A Variavel " + "'" + token.getLexema()+ "'" + " j� existe no escopo");
								break;
							}
								
						}
						aux--;
					}
					
					if(!errosemantico) {
						ArrayList<Variavel> vars = escopos.get(hierarquiaEscopoAtual);
						vars.add(v);
						break;
					}
				}
				
			} else {
				for (Variavel vv : variaveis) {
					if((vv.getNome().equals(token.getLexema()) && vv.getEscopo().equals(escopoAtual)) || (vv.getNome().equals(token.getLexema()) && vv.getEscopo().equals("global")) || (vv.getNome().equals(token.getLexema()) && vv.getEscopo().equals("programa"))) {
						errosemantico = true;
						errosSemanticos.add("Linha "+ token.getLinha() + " -> A Variavel " + "'" + token.getLexema()+ "'" + " j� existe no escopo");
						break;
					}
				}
				if(!errosemantico) {
					variaveis.add(v);
				}
			}
			identificador("Identificador");
			if (token.getLexema().equals("/")) {
				declaracaoMatriz();
			}
			listaVariavel(tipo);
			break;
		default:
			erroSintatico("Esperava uma declaracao de variavel");
			break;
		}
		
	}
	
	private void listaVariavel(String tipo) {
		boolean errosemantico = false;
		switch (token.getLexema()) {
		case ",":
			Variavel v = new Variavel();
			v.setTipo(tipo);
			terminal(",");
			v.setNome(token.getLexema());
			v.setEscopo(escopoAtual);
			
			if(!(escopoAtual.equals("programa")) && !(escopoAtual.equals("global"))){
				//ArrayList<Variavel> vars = escopos.get(hierarquiaEscopoAtual);
				int aux = hierarquiaEscopoAtual;
				for (ArrayList<Variavel> escopo : escopos) {
					
					while(aux > 0) {
						ArrayList<Variavel> vars = escopos.get(aux);
						for (Variavel var : vars) {
							if(var.getNome().equals(token.getLexema())){
								errosemantico = true;
								errosSemanticos.add("Linha "+ token.getLinha() + " -> A Variavel " + "'" + token.getLexema()+ "'" + " j� existe no escopo");
								break;
							}
								
						}
						aux--;
					}
					
					if(!errosemantico) {
						ArrayList<Variavel> vars = escopos.get(hierarquiaEscopoAtual);
						vars.add(v);
						break;
					}
				}
				
			} else {
				for (Variavel vv : variaveis) {
					if((vv.getNome().equals(token.getLexema()) && vv.getEscopo().equals(escopoAtual)) || (vv.getNome().equals(token.getLexema()) && vv.getEscopo().equals("global")) || (vv.getNome().equals(token.getLexema()) && vv.getEscopo().equals("programa"))) {
						errosemantico = true;
						errosSemanticos.add("Linha "+ token.getLinha() + " -> A Variavel " + "'" + token.getLexema()+ "'" + " j� existe no escopo");
						break;
					}
				}
				if(!errosemantico) {
					variaveis.add(v);
				}
			}
			
			
			
			identificador("Identificador");
			if (token.getLexema().equals("/")) {
				declaracaoMatriz();
			}
			listaVariavel(tipo);
			break;
		case ";":
			terminal(";");
			break;
		default:
			erroSintatico("Esperava um delimitador");
			sincroniza();
			listaVariavel(tipo);
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
			terminal("/");
			terminal("*");
			verificaTipo("Numero");
			terminal("*");
			terminal("/");
			if(token.getLexema().equals("/"))
				declaracaoMatriz();
			break;
		default:
			break;
		}
	}
	
	private void funcao() {
		switch (token.getLexema()) {
		case "funcao":
			token = proximo();
			String nome;
			if(token.getTipo().equals("Palavra Reservada")) {
				tipo();
				nome = token.getLexema();
				identificador("Identificador");
			} else {
				nome = token.getLexema();
				identificador("Identificador");
			}
			
			for (Funcao func : funcoes) {
				if(!(func.getNome().equals(nome))){
					errosSemanticos.add("Linha " + token.getLinha() + " -> A assinatura da funcao nao existe");
					break;
				}
			}
			//String nome = token.getLexema();
			escopoAtual = "programa";
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
			//Funcao f = new Funcao(tipo, nome);
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
			if (!(token.getLexema().equals(")")))
				retornoFuncao();
			terminal(")");
			terminal(";");
			break;

		default:
			erroSintatico("Esperava a funcao escreva");
			break;
		}
	}
	
	private void seEntao() {
		switch (token.getLexema()) {
		case "se":
			terminal("se");
			terminal("(");
			expressaoBooleana();
			terminal(")");
			terminal("entao");
			terminal("inicio");
			while (!(token.getLexema().equals("fim")))
				blocoDeCodigo();
			terminal("fim");
			if (token.getLexema().equals("senao"))
				seAninhado();
			//escopoAtual = "programa";
			break;
		default:
			erroSintatico("Esperava um bloco de 'se entao senao'");
			break;
		}
	}
	
	private void seAninhado() {
		escopos.remove(hierarquiaEscopoAtual);
		negacao();
		
	}

	private void negacao() {
		terminal("senao");
		terminal("inicio");
		escopoAtual = "senao";
		while (!(token.getLexema().equals("fim")))
			blocoDeCodigo();
		terminal("fim");
		//escopoAtual = "programa";
		
	}

	//BX
	private void blocoDeCodigo() {
		switch (token.getTipo()) {
		
		case "Palavra Reservada":
			blocoPalavraReservada();
			break;
			
		case "Identificador":
			Attr_temp();
			break;
		default:
			erroSintatico("Esperava um bloco de c�digo");
			break;
		}
		
	}

	private void Attr_temp() {
		boolean erroSemantico = true;
		boolean erroConstante = false;
		
		if(!(escopoAtual.equals("programa")) && !(escopoAtual.equals("global"))){
			int aux = hierarquiaEscopoAtual;
			for (ArrayList<Variavel> escopo : escopos) {
				
				while(aux > 0) {
					ArrayList<Variavel> vars = escopos.get(aux);
					for (Variavel var : vars) {
						if(token.getLexema().equals(var.getNome())){
							varAtual = var;
							erroSemantico = false;
							break;
						}
							
					}
					aux--;
				}
			}
			for (Variavel vv : variaveis) {
				if(vv.getNome().equals(token.getLexema())){
					varAtual = vv;
					erroSemantico = false;
				}
			}
			for (Constante c : constantes) {
				if(c.getId().equals(token.getLexema())){
					erroConstante = true;
				}
			}
			for (Funcao f : funcoes) {
				if(f.getNome().equals(token.getLexema())){
					ehFuncao = true;
				}
				
			}
		} else {
			for (Variavel vv : variaveis) {
				if(vv.getNome().equals(token.getLexema())){
					varAtual = vv;
					erroSemantico = false;
				}
			}
			for (Constante c : constantes) {
				if(c.getId().equals(token.getLexema())){
					erroConstante = true;
				}
			}
			for (Funcao f : funcoes) {
				if(f.getNome().equals(token.getLexema())){
					ehFuncao = true;
				}
				
			}
		}
		if(ehFuncao) {
			funcAtual = token.getLexema();
			//System.err.println("fodasse");
		}
		else if (erroSemantico == true && erroConstante == false) {
			errosSemanticos.add("Linha " + token.getLinha() + " -> Funcao " + "'" + token.getLexema() + "'" + " nao existe");
		}
		else if(erroSemantico && erroConstante) {
			errosSemanticos.add("Linha " + token.getLinha() + " -> Impossivel atribuir valor a uma constante");
		}
		else if(erroSemantico) {
			errosSemanticos.add("Linha " + token.getLinha() + " -> Variavel " + "'" + token.getLexema() + "'" + " nao existente no escopo");
	
		}
		identificador("Identificador");
		if(erroSemantico && !ehFuncao)
			sincroniza();
		Attr();
	}

	private void Attr() {
		//identificador("Identificador");
		aux_valor1();
		if (token.getLexema().equals(";")) {
			terminal(";");
		}
		else
			Attr1();
		
	}

	private void Attr1() {
		switch (token.getLexema()) {
		case "=":
			terminal("=");
			Attr2();
			break;
		default:
			break;
		}
		
	}
	

	private void Attr2() {
		boolean existe = false;
		if(varAtual != null) {
			valorAtual = token.getLexema();
		}
		switch (token.getTipo()) {
		case "Cadeia de caracteres":
			if(!(escopoAtual.equals("programa")) && !(escopoAtual.equals("global"))){
				int aux = hierarquiaEscopoAtual;
				for (ArrayList<Variavel> escopo : escopos) {
					
					while(aux > 0) {
						ArrayList<Variavel> vars = escopos.get(aux);
						for (Variavel var : vars) {
							if(token.getLexema().equals(var.getNome()) && !(var.getTipo().equals(varAtual.getTipo()))){
								errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
								existe = true;
								valorAtual = null;
								break;
							}
								
						}
						aux--;
					}
				}
				
				for(Variavel v : variaveis) {
					if(v.getNome().equals(token.getLexema()) && v.getTipo().equals(varAtual.getTipo())) {
						existe = true;
						valorAtual = null;
					} else if (v.getNome().equals(token.getLexema()) && !(v.getTipo().equals(varAtual.getTipo()))) {
						errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
						existe = true;
					}
				} 
				
				for (Constante c : constantes) {
					if(token.getLexema().equals(c.getId()) && c.getTipo().equals(varAtual.getTipo())){
						existe = true;
						valorAtual = null;
					} else if (token.getLexema().equals(c.getId()) && !(c.getTipo().equals(varAtual.getTipo()))) {
						if(varAtual.getTipo().equals("real") && c.getTipo().equals("inteiro")){
							existe = true;
						}
						else{
							errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
							existe = true;
						}
					}
				}
				
				if(!existe && token.getTipo().equals("Identificador"))
					errosSemanticos.add("Linha " + token.getLinha() + " -> Variavel " + "'" + token.getLexema() + "'" + " nao existente no escopo");
				
			} else {
				for(Variavel v : variaveis) {
					if(v.getNome().equals(token.getLexema()) && v.getTipo().equals(varAtual.getTipo())) {
						existe = true;
						valorAtual = null;
					} else if (v.getNome().equals(token.getLexema()) && !(v.getTipo().equals(varAtual.getTipo()))) {
						errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
						existe = true;
					}
				} 
				
				for (Constante c : constantes) {
					if(token.getLexema().equals(c.getId()) && c.getTipo().equals(varAtual.getTipo())){
						existe = true;
						valorAtual = null;
					} else if (token.getLexema().equals(c.getId()) && !(c.getTipo().equals(varAtual.getTipo()))) {
						if(varAtual.getTipo().equals("real") && c.getTipo().equals("inteiro")){
							existe = true;
						}
						else{
							errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
							existe = true;
						}
					}
				}
				
				if(!existe && token.getTipo().equals("Identificador"))
					errosSemanticos.add("Linha " + token.getLinha() + " -> Variavel " + "'" + token.getLexema() + "'" + " nao existente no escopo");
			}
			
			terminal(token.getLexema());
			varAtual = null;
			valorAtual = null;
			terminal(";");
			break;
		case "Caractere":
			if(!(escopoAtual.equals("programa")) && !(escopoAtual.equals("global"))){
				int aux = hierarquiaEscopoAtual;
				for (ArrayList<Variavel> escopo : escopos) {
					
					while(aux > 0) {
						ArrayList<Variavel> vars = escopos.get(aux);
						for (Variavel var : vars) {
							if(token.getLexema().equals(var.getNome()) && !(var.getTipo().equals(varAtual.getTipo()))){
								errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
								existe = true;
								valorAtual = null;
								break;
							}
								
						}
						aux--;
					}
				}
				
				for(Variavel v : variaveis) {
					if(v.getNome().equals(token.getLexema()) && v.getTipo().equals(varAtual.getTipo())) {
						existe = true;
						valorAtual = null;
					} else if (v.getNome().equals(token.getLexema()) && !(v.getTipo().equals(varAtual.getTipo()))) {
						errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
						existe = true;
					}
				} 
				
				for (Constante c : constantes) {
					if(token.getLexema().equals(c.getId()) && c.getTipo().equals(varAtual.getTipo())){
						existe = true;
						valorAtual = null;
					} else if (token.getLexema().equals(c.getId()) && !(c.getTipo().equals(varAtual.getTipo()))) {
						if(varAtual.getTipo().equals("real") && c.getTipo().equals("inteiro")){
							existe = true;
						}
						else{
							errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
							existe = true;
						}
					}
				}
				
				if(!existe && token.getTipo().equals("Identificador"))
					errosSemanticos.add("Linha " + token.getLinha() + " -> Variavel " + "'" + token.getLexema() + "'" + " nao existente no escopo");
				
			} else {
				for(Variavel v : variaveis) {
					if(v.getNome().equals(token.getLexema()) && v.getTipo().equals(varAtual.getTipo())) {
						existe = true;
						valorAtual = null;
					} else if (v.getNome().equals(token.getLexema()) && !(v.getTipo().equals(varAtual.getTipo()))) {
						errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
						existe = true;
					}
				} 
				
				for (Constante c : constantes) {
					if(token.getLexema().equals(c.getId()) && c.getTipo().equals(varAtual.getTipo())){
						existe = true;
						valorAtual = null;
					} else if (token.getLexema().equals(c.getId()) && !(c.getTipo().equals(varAtual.getTipo()))) {
						if(varAtual.getTipo().equals("real") && c.getTipo().equals("inteiro")){
							existe = true;
						}
						else{
							errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
							existe = true;
						}
					}
				}
				
				if(!existe && token.getTipo().equals("Identificador"))
					errosSemanticos.add("Linha " + token.getLinha() + " -> Variavel " + "'" + token.getLexema() + "'" + " nao existente no escopo");
			}
			terminal(token.getLexema());
			varAtual = null;
			valorAtual = null;
			terminal(";");
			break;
		default:
			if(!(escopoAtual.equals("programa")) && !(escopoAtual.equals("global"))){
				int aux = hierarquiaEscopoAtual;
				for (ArrayList<Variavel> escopo : escopos) {
					
					while(aux > 0) {
						ArrayList<Variavel> vars = escopos.get(aux);
						for (Variavel var : vars) {
							if(token.getLexema().equals(var.getNome()) && var.getTipo().equals(varAtual.getTipo())){
								//errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
								existe = true;
								valorAtual = null;
								break;
							}
								
						}
						aux--;
					}
				}
				
				for(Variavel v : variaveis) {
					if(v.getNome().equals(token.getLexema()) && v.getTipo().equals(varAtual.getTipo())) {
						existe = true;
						valorAtual = null;
					} else if (v.getNome().equals(token.getLexema()) && !(v.getTipo().equals(varAtual.getTipo()))) {
						if(varAtual.getTipo().equals("real") && v.getTipo().equals("inteiro")){
							existe = true;
						}
						else{
							errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
							existe = true;
						}
					}
				} 
				
				for (Constante c : constantes) {
					if(token.getLexema().equals(c.getId()) && c.getTipo().equals(varAtual.getTipo())){
						existe = true;
						valorAtual = null;
					} else if (token.getLexema().equals(c.getId()) && !(c.getTipo().equals(varAtual.getTipo()))) {
						if(varAtual.getTipo().equals("real") && c.getTipo().equals("inteiro")){
							existe = true;
						}
						else{
							errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
							existe = true;
						}
					}
				}
				if(!existe && token.getTipo().equals("Identificador")) {
					errosSemanticos.add("Linha " + token.getLinha() + " -> Variavel " + "'" + token.getLexema() + "'" + " nao existente no escopo");
				}
					
			} else {
				for(Variavel v : variaveis) {
					if(v.getNome().equals(token.getLexema()) && v.getTipo().equals(varAtual.getTipo())) {
						existe = true;
						valorAtual = null;
					} else if (v.getNome().equals(token.getLexema()) && !(v.getTipo().equals(varAtual.getTipo()))) {
						errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
						existe = true;
					}
				} 
				
				for (Constante c : constantes) {
					if(token.getLexema().equals(c.getId()) && c.getTipo().equals(varAtual.getTipo())){
						existe = true;
						valorAtual = null;
					} else if (token.getLexema().equals(c.getId()) && !(c.getTipo().equals(varAtual.getTipo()))) {
						if(varAtual.getTipo().equals("real") && c.getTipo().equals("inteiro")){
							existe = true;
						}
						else{
							errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
							existe = true;
						}
					}
				}
				
				if(!existe && token.getTipo().equals("Identificador")) {
					errosSemanticos.add("Linha " + token.getLinha() + " -> Variavel " + "'" + token.getLexema() + "'" + " nao existente no escopo");
					
				}
			}
			
			
			
			
			
			/*else if(varAtual != null && !verificaTipoSemantico())
				errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());*/
			valorAtual = null;
			expressaoAritmetica();
			varAtual = null;
			valorAtual = null;
			terminal(";");
			break;
		}
	}

	private void blocoPalavraReservada() {
		switch (token.getLexema()) {
			
		case "leia":
			leia();
			break;
			
		case "se":
			escopoAtual = "seentao";
			hierarquiaEscopoAtual++;
			seEntao();
			escopos.remove(hierarquiaEscopoAtual);
			hierarquiaEscopoAtual--;
			if(hierarquiaEscopoAtual == 0)
				escopoAtual = "programa";
			break;
			
		case "enquanto":
			escopoAtual = "enquanto";
			hierarquiaEscopoAtual++;
			enquanto();
			ArrayList<Variavel> vars = escopos.get(hierarquiaEscopoAtual);
			vars.remove(hierarquiaEscopoAtual);
			hierarquiaEscopoAtual--;
			if(hierarquiaEscopoAtual == 0)
				escopoAtual = "programa";
			break;
		case "escreva":
			escreva();
			break;
			
		case "var":
			declaracaoVariaveis();
			break;
		default:
			erroSintatico("Esperava um bloco de c�digo");
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
			terminal(">");
			auxExpression();
			break;
		case "<":
			terminal("<");
			auxExpression();
			break;
		case "<=":
			terminal("<=");
			auxExpression();
			break;
		case ">=":
			terminal(">=");
			auxExpression();
			break;
		case "=":
			terminal("=");
			auxExpression();
			break;
		case "<>":
			terminal("M>");
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
			verificaExistencia();
			//valor();
			break;

		default:
			valorAtual = null;
			verificaExistencia();
			//valor();
			break;
		}
		
	}

	private void verificaExistencia() {
		switch (token.getTipo()) {
		case "Numero":
			if(valorAtual == null)
				valorAtual = verificaTipo("Numero");
			else
				valorAtual = valorAtual + verificaTipo("Numero");
			if(varAtual != null && !verificaTipoSemantico())
				errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
			break;
		
		case "Digito":
			valorAtual = verificaTipo("Digito");
			if(varAtual != null && !verificaTipoSemantico())
				errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
			break;
			
		/*case "Palavra Reservada":
			if(token.getLexema().equals("verdadeiro")) {
				terminal("verdadeiro");
			} else if(token.getLexema().equals("falso")) {
				terminal("falso");
			}*/
			
		
		
		
		default:
			
			if(token.getLexema().equals("verdadeiro")) {
				terminal("verdadeiro");
			} else if(token.getLexema().equals("falso")) {
				terminal("falso");
			}
			
			else if(token.getLexema().equals("(")) {
				terminal("(");
				expressaoBooleana();
				terminal(")");
			} 
			
			else if(!(escopoAtual.equals("programa")) && !(escopoAtual.equals("global"))){
				boolean existe = false;
				int aux = hierarquiaEscopoAtual;
				for (ArrayList<Variavel> escopo : escopos) {
					
					while(aux > 0) {
						ArrayList<Variavel> vars = escopos.get(aux);
						for (Variavel var : vars) {
							if(token.getLexema().equals(var.getNome()) && (var.getTipo().equals("inteiro") || var.getTipo().equals("real"))){
								existe = true;
								break;
							}
								
						}
						aux--;
					}
				}
				
				for (Variavel vv : variaveis) {
					if(token.getLexema().equals(vv.getNome()) && (vv.getTipo().equals("inteiro") || vv.getTipo().equals("real"))) {
						existe = true;
					} 
				}
				for (Constante constante : constantes) {
					if(token.getLexema().equals(constante.getId()) && (constante.getTipo().equals("inteiro") || constante.getTipo().equals("real"))) {
						existe = true;
					}
				}
				
				if(!existe && token.getTipo().equals("Identificador"))
					errosSemanticos.add("Linha " + token.getLinha() + " -> A vvariavel ou constante '" + token.getLexema() + "' nao existe ou nao e do tipo inteiro ou real\n");
				token = proximo();
			}
			else {
				boolean existe = false;
				for (Variavel vv : variaveis) {
					if(token.getLexema().equals(vv.getNome()) && (vv.getTipo().equals("inteiro") || vv.getTipo().equals("real"))) {
						existe = true;
					} 
				}
				for (Constante constante : constantes) {
					if(token.getLexema().equals(constante.getId()) && (constante.getTipo().equals("inteiro") || constante.getTipo().equals("real"))) {
						existe = true;
					}
				}
				if(!existe)
					errosSemanticos.add("Linha " + token.getLinha() + " -> A vvariavel ou constante '" + token.getLexema() + "' nao existe ou nao e do tipo inteiro ou real\n");
				token = proximo();
			}
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
			if(varAtual != null)
				errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
			String t = null;
			if(token.getLexema().equals("verdadeiro")) {
				t = token.getLexema();
				terminal("verdadeiro");
			} else if(token.getLexema().equals("falso")) {
				t = token.getLexema();
				terminal("falso");
			}
			break;
		case "Identificador":
			if(varAtual != null) {
				for(Variavel v : variaveis) {
					if(token.getLexema().equals(v.getNome()) && !(v.getTipo().equals(varAtual.getTipo()))) {
						if(varAtual.getTipo().equals("real") && v.getTipo().equals("inteiro"))
							continue;
						else
							errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
						valorAtual = null;
					} 
				}
				for (Constante c : constantes) {
					if(c.getId().equals(token.getLexema()) && verificaTipoSemantico()){
						errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
						valorAtual = null;
					}
				}
			} else {
				boolean existe = false;
				for (Constante constante : constantes) {
					if(token.getLexema().equals(constante.getId()) && (constante.getTipo().equals("inteiro") || constante.getTipo().equals("real"))) {
						existe = true;
					}
				}
				for (Variavel vv : variaveis) {
					if(token.getLexema().equals(vv.getNome()) && (vv.getTipo().equals("inteiro") || vv.getTipo().equals("real"))) {
						existe = true;
					} 
				}
				
				if(!existe)
					errosSemanticos.add("Linha " + token.getLinha() + " -> A variavel ou constante '" + token.getLexema() + "' nao existe ou nao e do tipo inteiro ou real");
			}
			
			
			
			valorAtual = token.getLexema();
			identificador("Identificador");
			aux_valor1();
			break;
		case "Numero":
			if(valorAtual == null)
				valorAtual = verificaTipo("Numero");
			else
				valorAtual = valorAtual + verificaTipo("Numero");
			if(varAtual != null && !verificaTipoSemantico())
				errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
			break;
		case "Digito":
			valorAtual = verificaTipo("Digito");
			if(varAtual != null && !verificaTipoSemantico())
				errosSemanticos.add("Linha " + token.getLinha() + " -> Atriubicao nao e do tipo " + varAtual.getTipo());
			break;
		default:
			terminal("(");
			expressaoBooleana();
			terminal(")");
			break;
		}
	}
	
	private void retornoFuncao() {
		boolean existe = false;
		if(token.getTipo().equals("Identificador") || token.getTipo().equals("Numero") || token.getTipo().equals("Digito") || token.getTipo().equals("Caractere") || token.getTipo().equals("Cadeia de caracteres")){
			for(Variavel v : variaveis) {
				if(v.getNome().equals(token.getLexema())) {
					existe = true;
					valorAtual = null;
				}
			} 
			
			for (Constante c : constantes) {
				if(token.getLexema().equals(c.getId())){
					existe = true;
					valorAtual = null;
				}
			}
			if(!existe && token.getTipo().equals("Identificador"))
				errosSemanticos.add("Linha " + token.getLinha() + " -> Variavel " + "'" + token.getLexema() + "'" + " nao existente no escopo");
			token = proximo();
			verificaExpressaoBooleana();
		} else if (token.getLexema().equals("nao"))
			expressaoBooleana();
		else {
			erroSintatico("Retorno Incorreto");
			//token = proximo();
			//EXPRESSAO BOOLEANA
		}
	}
	
	private void verificaExpressaoBooleana() {
		if(token.getTipo().equals("Operador Aritmetico") || token.getTipo().equals("Operador Relacional") || token.getTipo().equals("Operador Logico")){
			contTokens--;
			contTokens--;
			token = proximo();
			expressaoBooleana();
		}
	}
	
	
	private void parametroFuncao() {
		int qtdParametros = 0;
		Variavel v = new Variavel();
		v.setTipo(token.getLexema());
		tipo();
		v.setNome(token.getLexema());
		identificador("Identificador");
		boolean existe = false;
		for (Variavel vars : parametrosFuncaoAux) {
			if(v.getNome().equals(vars.getNome()) && v.getTipo().equals(vars.getTipo())){
				existe = true;
			}
		}
		
		if(!existe) {
			parametrosFuncaoAux.add(v);
			existe = false;
		}
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

}
