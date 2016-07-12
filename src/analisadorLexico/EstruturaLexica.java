/**
 * 
 */
package analisadorLexico;

import java.util.ArrayList;

/**
 * @author afons
 *
 */
public class EstruturaLexica {

    private final ArrayList<String> palavrasReservadas  = new ArrayList<>();
    private final ArrayList<Character> letras  = new ArrayList<>();
    private final ArrayList<Character> digitos  = new ArrayList<>();
    private final ArrayList<Character> simbolosASCII  = new ArrayList<>();
    private final ArrayList<Character> operadoresAritmeticos  = new ArrayList<>();
    private final ArrayList<String> operadoresRelacionais  = new ArrayList<>();
    private final ArrayList<String> operadoresLogicos  = new ArrayList<>();
    private final ArrayList<Character> delimitadores  = new ArrayList<>();
    private final ArrayList<Character> comentarios  = new ArrayList<>();
    
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
		
		for (char i = 'a'; i <= 'z'; i++) {
            this.letras.add((char) i);
        }
		
		for (char i = 'A'; i <= 'Z'; i++) {
            this.letras.add((char) i);
        }
		
		for (char i = '0'; i <= '9'; i++) {
            this.digitos.add(i);
        }
		
        operadoresAritmeticos.add('+');
        operadoresAritmeticos.add('-');
        operadoresAritmeticos.add('*');
        operadoresAritmeticos.add('/');
        
        operadoresRelacionais.add("<>");
        operadoresRelacionais.add("=");
        operadoresRelacionais.add("<");
        operadoresRelacionais.add("<=");
        operadoresRelacionais.add(">");
        operadoresRelacionais.add(">=");
        
        operadoresLogicos.add("nao");
        operadoresLogicos.add("e");
        operadoresLogicos.add("ou");
        
        delimitadores.add(';');
        delimitadores.add(',');
        delimitadores.add('(');
        delimitadores.add(')');
        
        comentarios.add('{');
        comentarios.add('}');
        
        // Inserindo inserindo c�digos ASCII do 32 ao 126.
        for (int i = 32; i <= 126; i++) {
            this.simbolosASCII.add((char) i);
        }
	}

}
