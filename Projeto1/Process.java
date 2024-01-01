
public class Process {
    int tempoChegada;
    int duracao;
    int id;
    int finalizado = 0;
    int prioridade1 = 0;
    int duracaoRestante = 0;
    int posicao = -1;
    int tempoResp = 0;
    int pegarPos = 0;
    int primeiraVez = 0;

    public Process(int tempoChegada, int duracao) {
        this.tempoChegada = tempoChegada;
        this.duracao = duracao;
        this.duracaoRestante = duracao;
    }
}