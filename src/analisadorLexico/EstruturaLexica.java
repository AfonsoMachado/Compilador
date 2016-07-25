/**
 * 
 */
package analisadorLexico;

import java.util.ArrayList;

/**
 * @author Afonso Machado
 * @author Henderson Chalegre
 *
 */
public class EstruturaLexica {

    /**
     * 
     */
    private final ArrayList<String> palavrasReservadas  = new ArrayList<>();
    /**
     * 
     */
    private final ArrayList<Character> operadoresAritmeticos  = new ArrayList<>();
    /**
     * 
     */
    private final ArrayList<Character> operadoresRelacionais  = new ArrayList<>();
    /**
     * 
     */
    private final ArrayList<String> operadoresLogicos  = new ArrayList<>();
    /**
     * 
     */
    private final ArrayList<Character> delimitadores  = new ArrayList<>();
    
	/**
	 * 
	 */
	public EstruturaLexica() {
		
		palavrasReservadas.add("programa");
		palavrasReservadas.add("const");
		palavrasReservadas.add("var");
		palavrasReservadas.add("funcao");
		palavrasReservadas.add("inicio");
		palavrasReservadas.add("fim");
		palavrasReservadas.add("se");
		palavrasReservadas.add("entao");
		palavrasReservadas.add("senao");
		palavrasReservadas.add("enquanto");
		palavrasReservadas.add("faca");
		palavrasReservadas.add("leia");
		palavrasReservadas.add("escreva");
		palavrasReservadas.add("inteiro");
		palavrasReservadas.add("real");
		palavrasReservadas.add("booleano");
		palavrasReservadas.add("verdadeiro");
		palavrasReservadas.add("falso");
		palavrasReservadas.add("cadeia");
		palavrasReservadas.add("caractere");
		
        operadoresAritmeticos.add('+');
        operadoresAritmeticos.add('-');
        operadoresAritmeticos.add('*');
        operadoresAritmeticos.add('/');
        
        operadoresRelacionais.add('<');
        operadoresRelacionais.add('=');
        operadoresRelacionais.add('>');
        
        operadoresLogicos.add("nao");
        operadoresLogicos.add("e");
        operadoresLogicos.add("ou");
        
        delimitadores.add(';');
        delimitadores.add(',');
        delimitadores.add('(');
        delimitadores.add(')');
        
	}
	
	/**
	 * @param s
	 * @return
	 */
	public boolean isPalavraResevada(String s){
		return this.palavrasReservadas.contains(s);
	}
	
	/**
	 * @param c
	 * @return
	 */
	public boolean isDelimitador(char c) {
		return this.delimitadores.contains(c);
	}
	
	/**
	 * @param c
	 * @return
	 */
	public boolean isOperador(char c) {
		if(this.operadoresAritmeticos.contains(c) || this.operadoresRelacionais.contains(c))
			return true;
		return false;
	}
	
	/**
	 * @param c
	 * @return
	 */
	public boolean isOperadorAritimetico(char c) {
		return this.operadoresAritmeticos.contains(c);
	}
	
	/**
	 * @param s
	 * @return
	 */
	public boolean isOperadorLogico(String s){
		return this.operadoresLogicos.contains(s);
	}
	
	/**
	 * @param ch
	 * @return
	 */
	public boolean isSpace(char ch){    
        return (Character.isSpaceChar(ch) || ch == 9);
    }

}
