/**
    * Componente Curricular: M�dulo Integrado de Concorrencia e Conectividade
    * Autor: <Henderson Souza Chalegre>
    * Data:  <19 de setembro de 2013>
    *
    * Declaro que este c�digo foi elaborado por mim de forma individual e
    * n�o cont�m nenhum trecho de c�digo de outro colega ou de outro autor, 
    * tais como provindos de livros e apostilas, e p�ginas ou documentos 
    * eletr�nicos da Internet. Qualquer trecho de c�digo de outra autoria que
    * uma cita��o para o  n�o a minha est� destacado com  autor e a fonte do
    * c�digo, e estou ciente que estes trechos n�o ser�o considerados para fins
    * de avalia��o. Alguns trechos do c�digo podem coincidir com de outros
    * colegas pois estes foram discutidos em sess�es tutorias.
    */
  package old;
    
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