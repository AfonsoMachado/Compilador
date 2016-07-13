/**
    * Componente Curricular: Módulo Integrado de Concorrencia e Conectividade
    * Autor: <Henderson Souza Chalegre>
    * Data:  <19 de setembro de 2013>
    *
    * Declaro que este código foi elaborado por mim de forma individual e
    * não contém nenhum trecho de código de outro colega ou de outro autor, 
    * tais como provindos de livros e apostilas, e páginas ou documentos 
    * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
    * uma citação para o  não a minha está destacado com  autor e a fonte do
    * código, e estou ciente que estes trechos não serão considerados para fins
    * de avaliação. Alguns trechos do código podem coincidir com de outros
    * colegas pois estes foram discutidos em sessões tutorias.
    */
  package br.uefs.ecomp.util;
    
  import java.io.File;
  import java.io.FileNotFoundException;
  import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
  import java.util.ArrayList;
    
    
  public class Paser {
    	
  public static Paser estance;
   
  /**
   * 
   * @return
   */
  public static Paser getEstance() {
	
  if (estance == null){
  
  estance = new Paser();
  }

  return estance;
  }
    
 
  
  
  /**
   * Le o arquivo e armazena as palavras encontradas
   * @param titulo
   * @return
   * @throws FileNotFoundException
   */
    public ArrayList<String>  paser(String titulo) throws FileNotFoundException{
		
		ArrayList<String> paser = new ArrayList<>();
		File arquivo = new File(titulo);
		
		Scanner scanner = new Scanner(new FileReader(arquivo));
				 
		while (scanner.hasNextLine()) {
		
		String linha = scanner.nextLine();
		
		
		
		paser.add(linha);
		}
		
		return paser;
		}


  
  public void gravar (ArrayList<String> l ,	String a) throws IOException{
	  
	  FileWriter arq = new FileWriter(a); 
		PrintWriter gravarArq = new PrintWriter(arq); 
		String b;
		
		while (!l.isEmpty()){ 
			
			b = l.remove(0);
			
			if(!b.equals(""))
				gravarArq.println(b);
			//.printf(b);
		
		}
		  
		arq.close();
	  
	  
  }
    
    }//fim da classe    