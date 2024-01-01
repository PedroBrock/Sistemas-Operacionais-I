import java.io.*;
import java.util.*;

public class Substituição_de_páginas {

	public static void main(String[] args) {
		try {
		
			Scanner scanner = new Scanner(new File("teste1.txt")); // Ler o arquivo com o scanner 
			
			List<Integer> sequencia_referencias = new ArrayList<>(); // Cria uma lista que vai guardar a sequência de referência

			int num_quadros = scanner.nextInt(); // O primeiro número do arquivo vai ser o número de quadros disponíveis
	        while (scanner.hasNext()) {
	            sequencia_referencias.add(scanner.nextInt());
	        }
	        
	        Queue<Integer> quadrosFIFO = new ArrayDeque<>(num_quadros); /* Cria uma fila para saber quais referências 
	        																		estão nos quadros*/
	        int faltasFIFO = 0; 

	        for (int pagina : sequencia_referencias) { // Um loop para cada referência
	        	
	            if (!quadrosFIFO.contains(pagina)) { 	/* Verifica se a página está no quadro se não estiver adiciona ela
	            									   no inicio do quadro e incrementa a variavel faltas*/
	                if (quadrosFIFO.size() == num_quadros) { 		
	                	quadrosFIFO.poll(); // Removo a cabeça da lista
	                }
	                quadrosFIFO.offer(pagina); // Insiro a página na cabeça da lista
	                faltasFIFO++;
	            }
	        }
	        
	        System.out.println("FIFO " + faltasFIFO);
	        
	        // Começa o Algoritmo Ótimo
	        
	        List<Integer> quadrosOTIMO = new ArrayList<>(num_quadros); /* Cria uma lista para saber quais referências 
			estao nos quadros*/
	        int faltasOTIMO = 0;
	        
	        for (int i = 0; i < sequencia_referencias.size(); i++) {
	            int pagina = sequencia_referencias.get(i);

	            if (!quadrosOTIMO.contains(pagina)) {  	  		// Continua se a pagina não estiver no quadro
	                if (quadrosOTIMO.size() < num_quadros) {
	                	quadrosOTIMO.add(pagina);				// Se o quadro não estar cheio coloco a pagina lá
	                } else {									// Se o quadro estar cheio crio as variaveis indice e maximo
	                    int indice = -1;  						// Serve para indicar qual página vai sair
	                    int maximo = -1;						// Serve para indicar a próxima ocorrência

	                    for (int j = 0; j < quadrosOTIMO.size(); j++) {
	                        int proximaOcorrencia = sequencia_referencias.subList(i + 1, sequencia_referencias.size()).indexOf(quadrosOTIMO.get(j));
	                        if (proximaOcorrencia == -1) {
	                            indice = j;					    // Guardo o índice da página que vai sair
	                            break;
	                        } else if (proximaOcorrencia > maximo) {
	                            maximo = proximaOcorrencia; 	// Guardo a próxima ocorrência da página
	                            indice = j;						// Guardo o índice da página que vai sair
	                        }
	                    }

	                    quadrosOTIMO.set(indice, pagina); 		// Atualiza a lista de quadros
	                }

	                faltasOTIMO++;
	            }
	        }
	        System.out.println("OTM " + faltasOTIMO);
	        
	        
	        // Começando LRU
	        int faltasLRU = 0;
	        List<Integer> quadrosLRU = new ArrayList<>(num_quadros);/* Cria uma lista para saber quais referências 
			estao nos quadros*/
	        HashMap<Integer, Integer> ultimaUtilizacao = new HashMap<>(); /* Guardar o indice da ultima vez 
	        																 que a página foi utilizada */

	        for (int i = 0; i < sequencia_referencias.size(); i++) {
	            int pagina = sequencia_referencias.get(i);

	            ultimaUtilizacao.put(pagina, i);  // Atualizo o valor de ultimaUtilizacao com a página atual e seu índice atual

	            if (!quadrosLRU.contains(pagina)) {
	                if (quadrosLRU.size() < num_quadros) {   // Se o quadro não estar cheio coloco a página lá
	                	quadrosLRU.add(pagina);
	                } else {
	                    int indice = 0;
	                    int menorUltimaUtilizacao = Integer.MAX_VALUE; // Variável de comparação
	                    												/* Server para encontrar a página 
	                    												com a menor utilização recente */

	                    for (int j = 0; j < quadrosLRU.size(); j++) {
	                        int quadro = quadrosLRU.get(j);			// Pego o quadro na posição atual
	                        int ultima = ultimaUtilizacao.get(quadro); 
	                        if (ultima < menorUltimaUtilizacao) { // Verifico se a sua utilização foi realmente a última
	                            menorUltimaUtilizacao = ultima;   // Se for coloco essa página como a última a ser utilizada
	                            indice = j;						  // Guardo o índice da página que vai sair
	                        }
	                    }

	                    quadrosLRU.set(indice, pagina); // Atualiza a lista de quadros 
	                }

	                faltasLRU++;
	            }
	        }
	        System.out.println("LRU " + faltasLRU);
	        
	        
	        scanner.close();
		} catch (FileNotFoundException e) { 
            e.printStackTrace();
        }	
	}

}
