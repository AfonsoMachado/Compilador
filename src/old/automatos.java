package old;


public class automatos {
	
	
	
	/* Esse metodo verifica se a sequencia pasada eh um caracter
	 * 
	 * Sequencia obrigatoria: 'letra | Digito '
	*/
	public boolean ehCaractere(String c){
		
		
		// i serve pra mudar os estados
		// b pra pecorrer a string
		int i = 0, b = 0; 
		
		//Se a String recebida tiver o tamanho diferente de 3 é descartado
		if (c.length() != 3){
			
			i=9; // valor invalido 
		}
		
		String a = "'";
		
		//pecorre enquanto b for menor que o tamanho da string
		//ou se i não for invalido
			while (b < c.length()  && i!=9 ){
			
			switch (i){
			
			   //Estado 0
			   case 0: 
				   
				   //Verifica se o primeiro digito eh '
				   if (c.charAt(b) == a.charAt(0)){
					   
				   i = 1;// posiciona pro proximo estado
				   b++; // muda a posicao atual
				   
				   }else{ 
					   i = 9;}// valor invalido
				   
				//////////////////////////////////////
				
				  // Estado 1 
			   case 1 :   
				   
				   //Verifica se eh um digito ou uma letra
				   if (Character.isLetterOrDigit(c.charAt(b))){
					   
					   
					   i = 2;// posiciona pro proximo estado
					   b++; // muda a posicao atual
					   
					   }else{ 
						   i = 9;}// valor invalido
				  
				   //////////////////////////////////////
					
				   // Estado 2
				   
			   case 2: 
				   if (c.charAt(b) == a.charAt(0)){
					   
				   i = 3;// posiciona pro proximo estado
				   b++; // muda a posicao atual
				   
				   }else{ 
					   i = 9;}// valor invalido
				   
				 ///////////////////////////////////////				
			
				   //Fim da maquina
				   
				   
			}//switch
	
	
			
			}
			// Se chegar no estado final retorna verdadeiro
			if (i == 3){
				return true;}
			    return false;
	}
	
	
	/* Esse metodo verifica se a sequencia pasada eh um digito
	 * 
	 * Sequencia obrigatoria: [-] Digito {Digito } [. Digito{Digito}]
	*/
	
	public boolean ehDigito(String veri){
		
		int i = 0, b = 0;
		
		
		while (b < veri.length()  && i!=9 ){
			
			switch (i){
			  
			 //////////////////////////////////////
			
			   //Estado 0	
			
			case 0: 
				
				if (veri.charAt(b)== '-' ){
					
					i = 1;// posiciona pro proximo estado
					b++; // muda a posicao atual
				}  
				
				
				if (Character.isDigit(veri.charAt(b))){
					
					i = 2;// posiciona pro proximo estado
					b++; // muda a posicao atual
					
					
				}else{
					
					i = 9; // valor invalido 
				}// else case 0
			
				 //////////////////////////////////////
				
				   //Estado 1
				
			   case 1: 
				
				   if (Character.isDigit(veri.charAt(b))){
						
						i = 2;// posiciona pro proximo estado
						b++; // muda a posicao atual
						
						
						
					}else{
						
						i = 9;// valor invalido 
					}
				   
				   
				   //////////////////////////////////////
					
				   //Estado 2
			   case 2: 
					
					if (Character.isDigit(veri.charAt(b))){
						
						i = 2;// posiciona pro proximo estado
						b++; // muda a posicao atual
						
					}
					
					if(veri.charAt(b)== '.'){
						
						i = 3;// posiciona pro proximo estado
						b++; // muda a posicao atual
							
					}else{
						
						i = 9;// valor invalido 
					   
					}
				
					
					 //////////////////////////////////////
					
					   //Estado 3
			   case 3:
				   
				   if (Character.isDigit(veri.charAt(b))){
					   
					i = 4;// posiciona pro proximo estado
					b++; // muda a posicao atual
					
				   }else{
						i = 9;// valor invalido 
					}
				
				   
				   //////////////////////////////////////
					
				   //Estado 4
				   
			   case 4:
				   
				   if (Character.isDigit(veri.charAt(b))){
					i = 4;// posiciona pro proximo estado
					b++; // muda a posicao atual
					
				   }else{
						i = 9;// valor invalido 
					}
				   
				   
				   
				   
				   
				   
				
			}  //switch
			
			
			
			
		}
		
		if(i ==1 || i == 3){return true;}
		return false;
		
	}

	/* Esse metodo verifica se a sequencia pasada eh um identificador
	 * 
	 * Sequencia obrigatoria: letra{letra | Digito |_}
	*/
	private static boolean isIdentificador (String veri){
		
		int i = 0, b = 0;
		
		while (b < veri.length() && i!=9 ){
			
			switch (i){
			
			 //////////////////////////////////////
			
			   //Estado 0
			case 0: 
				
				if (Character.isLetter(veri.charAt(b))){
				
					i = 1;
					b ++;
							
			    } else {
			    	
			    	i = 9;// valor invalido 
			    }
				 //////////////////////////////////////
				
				   //Estado 1
			case 1: 	
				
				
				if (Character.isLetterOrDigit(veri.charAt(b)) ||
						veri.charAt(b)== '_'  ){
				
					b ++;
				
					
					
				}else {
			    	
			    	i = 9;// valor invalido 
			    }
				
				
				}
			
		
		
		}//switch
		
		
		if(i == 1){
			return true;
		}
		return false;
	}
	
	
	
	
	
}
