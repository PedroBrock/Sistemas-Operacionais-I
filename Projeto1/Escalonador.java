import java.io.*;
import java.util.*;

public class Escalonador {
    public static void main(String[] args) {
        try {
        	int tempoAtual = 0, i = 0, processoFinalizados = 0, ocioso = 0;
        	float tempoRetornoFCFS = 0, tempoRespostaFCFS = 0, tempoEsperaFCFS = 0;
        	float tempoRetorno, tempoResposta, tempoEspera;
        	
            Scanner scanner = new Scanner(new File("teste10.txt")); // Ler o arquivo com o scanner 
            List<Process> processosFCFS = new ArrayList<>(); 	   // Prepara o array para colocar os processos
            List<Process> processosSJF = new ArrayList<>();
            List<Process> processosRR = new ArrayList<>();

            while (scanner.hasNext()) {
                int tempoChegada = scanner.nextInt(); // Ler os dados do arquivo e coloca no objeto process
                int duracao = scanner.nextInt();
                processosFCFS.add(new Process(tempoChegada, duracao)); // Adiciona os processos no array
                processosSJF.add(new Process(tempoChegada, duracao));
                processosRR.add(new Process(tempoChegada, duracao));
            }

            Collections.sort(processosFCFS, Comparator.comparingInt(p -> p.tempoChegada)); // Ordena os processos em ordem de chegada
            
            for (Process processo : processosFCFS) {
            	processo.id = i;
            	i++;
            }
            
            for (Process processo : processosFCFS) { // Ver se o P1 realmente começa no tempo = 0
            	if(processo.id == 0) {
            		if(processo.tempoChegada>tempoAtual) {
            			tempoAtual = processo.tempoChegada;
            		}
            	}
            }
            
            while(processoFinalizados != processosFCFS.size()) {
            for (Process processo : processosFCFS) { // Para cada processo fazemos isso
            	if(processo.tempoChegada<=tempoAtual && (processo.finalizado == 0)) {
                tempoRetorno = tempoAtual + processo.duracao - processo.tempoChegada; // tempo de retorno do processo
                tempoResposta = tempoAtual - processo.tempoChegada; // tempo de resposta do processo
                tempoEspera = tempoResposta; // tempo de espera do processo

                tempoAtual +=  processo.duracao; // Incremento o tempo atual
                tempoRetornoFCFS += tempoRetorno; // Guardo o tempo de retorno total
                tempoRespostaFCFS += tempoResposta; // Guardo o tempo de resposta total
                tempoEsperaFCFS += tempoEspera; // Guardo o tempo de espera total
                processoFinalizados++;
                processo.finalizado = 1;
                ocioso = 0;
            	}else if(ocioso == 0){
            		tempoAtual = processo.tempoChegada;
            		ocioso = 1;
            	}
            }
            }
            
            float mediaRetornoFCFS = tempoRetornoFCFS / processosFCFS.size(); // Calculo a media do tempo de retorno 
            float mediaRespostaFCFS = tempoRespostaFCFS / processosFCFS.size(); // Calculo a media do tempo de resposta
            float mediaEsperaFCFS = tempoEsperaFCFS / processosFCFS.size(); // Calculo a media do tempo de espera

            System.out.println("FCFS " + mediaRetornoFCFS + " " + mediaRespostaFCFS + " " + mediaEsperaFCFS);
            
            for (Process processo : processosFCFS) {
            	processo.finalizado = 0;
            }
        
            // Começa o SJF
            
            float tempoRetornoTotal = 0, tempoRespostaTotal = 0, tempoEsperaTotal = 0;
            tempoAtual = 0;
            ocioso = 0;
            i = 0;
            processoFinalizados = 0;
            
            
            
            Collections.sort(processosSJF, Comparator.comparingInt(p -> p.duracao)); // Ordena os processos por duração
            
            int prio1 = 0; 
            
            for(Process processo : processosSJF) {
            	processo.prioridade1 = prio1; // Dar a prioridade por duracao
            	prio1++;
            }
            
            Collections.sort(processosSJF, Comparator.comparingInt(p -> p.tempoChegada));
            
            for (Process processo : processosSJF) { 
            	processo.id = i; // Dar o id pelo tempo de chegada
            	i++;
            }
            
            for (Process processo : processosSJF) { // Ver se o P1 realmente começa no tempo = 0
            	if(processo.id == 0) {
            		if(processo.tempoChegada>tempoAtual) {
            			tempoAtual = processo.tempoChegada;
            		}
            	}
            }
            
            for(Process processo : processosSJF){ 
            	if(tempoAtual>=processo.tempoChegada && processo.finalizado == 0 && (processo.id == 0 || processo.prioridade1 == 0)) {
            		int tempoResposta2 = tempoAtual - processo.tempoChegada;
            		tempoAtual += processo.duracao;
            		int tempoRetorno2 = tempoAtual - processo.tempoChegada;
            		int tempoEspera2 = tempoResposta2;
            		
            		tempoRetornoTotal += tempoRetorno2; // Apenas os processo que chegaram até agora entram 
                    tempoRespostaTotal += tempoResposta2; // Com preferencia os de menores duraçoes
                    tempoEsperaTotal += tempoEspera2;
                    processo.finalizado = 1;
                    processoFinalizados++;
            	}
            }
            
            if(processoFinalizados != processosSJF.size()) { // Se os processos nao finalizados ainda nao tiverem chegado
            	for(Process processo : processosSJF) {      // Esperar por eles
            		if(processo.tempoChegada>tempoAtual && processo.finalizado == 0 && ocioso == 0) {
            			tempoAtual = processo.tempoChegada;
            			ocioso = 1;
            		}
            	}
            }
            
            
            Collections.sort(processosSJF, Comparator.comparingInt(p -> p.duracao)); // Ordenar por duracao novamente
            Collections.sort(processosSJF, Comparator.comparingInt(p -> p.finalizado));
           
            do {
            for(Process processo : processosSJF) { // Terminar os processos que ainda estao esperando
            	if(tempoAtual>=processo.tempoChegada && processo.finalizado == 0) { 
            		int tempoResposta2 = tempoAtual - processo.tempoChegada;
            		tempoAtual += processo.duracao;
            		int tempoRetorno2 = tempoAtual - processo.tempoChegada;
            		int tempoEspera2 = tempoResposta2;
            		
            		tempoRetornoTotal += tempoRetorno2;
                    tempoRespostaTotal += tempoResposta2;
                    tempoEsperaTotal += tempoEspera2;
                    processo.finalizado = 1;
                    processoFinalizados++;
            	}
            }
            }while(processoFinalizados != processosSJF.size()); // até acabar os processos
            
            float RetornoMedioSJF = tempoRetornoTotal / processosSJF.size(); 
            float RespostaMedioSJF = tempoRespostaTotal / processosSJF.size(); // Calcular metricas
            float EsperaMedioSJF = tempoEsperaTotal / processosSJF.size();

            System.out.println("SJF " + RetornoMedioSJF + " " + RespostaMedioSJF + " " + EsperaMedioSJF);
            
            // Comeca o RR
            
            int qq = 2;
            tempoAtual = 0;
            tempoRetornoTotal = 0;
            tempoRespostaTotal = 0;
            tempoEsperaTotal = 0;
            processoFinalizados = 0;
            ocioso = 0;
            
            List<Process> prontos = new ArrayList<>();

            Collections.sort(processosRR, Comparator.comparingInt(p -> p.tempoChegada));
            
            for (Process processo : processosRR) { 
            	if(processo == processosRR.get(0)) {
            			tempoAtual = processo.tempoChegada;
            			prontos.add(processo);
            	}
            }

            while(processoFinalizados != processosRR.size()){
            	for (Process processo : processosRR) {
            		if(prontos.contains(processo)) {
            			if (processo.duracaoRestante > 0) {
            			if (processo.duracaoRestante <= qq) { // O processo é concluído
                          	tempoAtual += processo.duracaoRestante;
                          	tempoRetornoTotal += tempoAtual - processo.tempoChegada;
                          	tempoEsperaTotal += tempoAtual - processo.tempoChegada - processo.duracao;
                          	processo.duracaoRestante = 0;
                          	processoFinalizados++;
                          	prontos.remove(processo);
                          	processo.finalizado = 1;
                          	ocioso = 1;
                          } else{ // O quantum expira, mas o processo ainda não está concluído
                          	if(processo.primeiraVez==0) {
                          		tempoRespostaTotal += tempoAtual - processo.tempoChegada;
                          		processo.tempoResp += tempoAtual - processo.tempoChegada;
                          		processo.primeiraVez = 1;
                          	}
                          	processo.duracaoRestante -= qq;
                          	tempoAtual += qq;
                          	prontos.remove(processo);
                          }
            			}
            		}else if(processo.tempoChegada<=tempoAtual){
            			prontos.add(processo);
            			ocioso = 0;
            		}else if(ocioso == 1 && processo.tempoChegada>tempoAtual) {
            			tempoAtual = processo.tempoChegada;
            			ocioso = 0;
            		}
            	}
            }
            
            float media_retorno = tempoRetornoTotal / processosRR.size(); // Calculo a media do tempo de retorno 
            float media_resposta = tempoRespostaTotal / processosRR.size(); // Calculo a media do tempo de resposta
            float media_espera = tempoEsperaTotal / processosRR.size(); // Calculo a media do tempo de espera

            System.out.println("RR " + media_retorno + " " + media_resposta + " " + media_espera);
            
            
            scanner.close();
        } catch (FileNotFoundException e) { // Colocar para capturar essa exceção para não dar erro ao compilar
            e.printStackTrace();
        }
    }
}