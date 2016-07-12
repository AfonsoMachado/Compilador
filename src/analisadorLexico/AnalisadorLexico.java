package analisadorLexico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class AnalisadorLexico {
	
	private FileReader fileR;//atributo de leitura de arquivo
    private BufferedReader readFile;//atributo de buffer de leitura de arquivo
    private FileWriter fileW;//atributo de escrita de arquivo
    private BufferedWriter writeFile;//atributo de buffer de escrita de arquivo

	
	private static final char EOF = '\0';
	private final EstruturaLexica estruturaLexica = new EstruturaLexica();

	public static void main(String[] args) {
		
		
	}

}
