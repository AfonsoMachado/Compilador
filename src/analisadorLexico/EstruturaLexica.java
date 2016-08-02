/**
 * 
 */
package analisadorLexico;

import java.util.ArrayList;

/**
 * cont�m todos os elementos que comp�em a estrutura l�xica da linguagem, tais como as palavras reservadas, os operadores l�gicos, aritm�ticos e relacionais, os delimitadores e os s�mbolos que n�o s�o aceitos e os que s�o aceitos na linguagem, tornando assim mais simples a minipula��o dessa estrutura por meio do analisador l�xico
 * 
 * @author Afonso Machado
 * @author Henderson Chalegre
 *
 */
public class EstruturaLexica {

    /**
     * ArrayList que armazena todas as palavras reservadas da lingaugem
     */
    private final ArrayList<String> palavrasReservadas  = new ArrayList<>();
    /**
     * ArrayList que armazena todos os operadores aritim�ticos da linguagem
     */
    private final ArrayList<Character> operadoresAritmeticos  = new ArrayList<>();
    /**
     * ArrayLisy que armazena todos os operadores relacionais da linguagem
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
    private final ArrayList<Character> simbolos = new ArrayList<>();
    /**
     * 
     */
    private final ArrayList<Character> letras = new ArrayList<>();
    
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
        
        for (int i = 32; i <= 126; i++) {
            this.simbolos.add((char) i);
        }
        
        for (char i = 'a'; i <= 'z'; i++) {
            this.letras.add((char) i);
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            this.letras.add((char) i);
        }
        
	}
	
	/**
	 * Verifica se uma String � uma palavra reservada
	 * 
	 * @param s String para ser comparada com a lista de palavras reservadas
	 * @return verdadeiro se a String s for uma palavra reservada, falso caso contr�rio
	 * 
	 */
	public boolean isPalavraResevada(String s){
		return this.palavrasReservadas.contains(s);
	}
	
	/**
	 * Verifica se � uma letra v�lida, maiuscula ou minuscula
	 * 
	 * @param c Char para ser comparado com a lista de letras
	 * @return verdadeiro se o char for uma letra e falso caso contrario
	 */
	public boolean isLetra(char c) {
		return this.letras.contains(c);
	}
	
	/**
	 * Verifica se um char � uma delimitador v�lido
	 * 
	 * @param c Char para ser comparado com a lista de delimitadores
	 * @return verdadeiro se o Char c for um delimitador, falso caso contr�rio
	 * 
	 */
	public boolean isDelimitador(char c) {
		return this.delimitadores.contains(c);
	}
	
	/**
	 * Verifica se um char � um operador aritim�tico ou um operador relacional v�lido
	 * 
	 * @param c Char para ser comparado com a lista de operadores aritm�ticos e relacionais
	 * @return verdadeiro se o Char c for um operador aritm�tico ou relacional, falso caso contr�rio
	 * 
	 */
	public boolean isOperador(char c) {
		if(this.operadoresAritmeticos.contains(c) || this.operadoresRelacionais.contains(c))
			return true;
		return false;
	}
	
	/**
	 * Verifica se um char � um operador l�gico v�lido
	 * 
	 * @param s Char para ser comparado com a lista de operadores l�gicos
	 * @return verdadeiro se a String s for um operador l�gico, falso caso contr�rio
	 * 
	 */
	public boolean isOperadorLogico(String s){
		return this.operadoresLogicos.contains(s);
	}
	
	/**
	 * Verifica se um char � um espa�o, um tab ou uma quebra de linha
	 * 
	 * @param ch Char para a verfica��o de espa�o, tab ou quebra de linha
	 * @return verdadeiro se o Char ch for um espa�o, tab ou quebra de linha, falso caso contr�rio
	 * 
	 */
	public boolean isSpace(char ch){    
        return (Character.isSpaceChar(ch) || ch == 9);
    }

}
