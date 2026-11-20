package siga;
/**
 * PROBLEMA 2 - o ciclo de vida resolvido com condicionais sobre um campo
 * de texto.
 *
 * Tres sintomas: (a) as regras de transicao estao espalhadas pelos metodos
 * publicos, e ninguem le o ciclo de vida inteiro num lugar so; (b) um
 * estado novo exige alterar todos eles; e (c) a situacao e texto, entao um
 * erro de digitacao so aparece em execucao.
 *
 * Ciclo de vida vigente - E ELE QUE DEVE SER PRESERVADO:
 *   RESERVADO  --emprestar-->  EMPRESTADO
 *   RESERVADO  --anular-->  ENCERRADO
 *   EMPRESTADO  --restituir-->  ENCERRADO
 *   ENCERRADO e final: recusa as tres operacoes.
 */

public class Emprestimo {

    private final Leitor solicitante;
    private final Exemplar item;
    private EstadoEmprestimo estado;

    public Emprestimo(Leitor solicitante, Exemplar item) {
        this.solicitante = solicitante;
        this.item = item;
        this.estado = new EstadoReservado();
    }

    void alterarEstado(EstadoEmprestimo novoEstado) {
        this.estado = novoEstado;
    }

    public String getSituacao() {
        return estado.getSituacao();
    }

    public void emprestar() {
        estado.emprestar(this);
    }

    public void restituir() {
        estado.restituir(this);
    }

    public void anular() {
        estado.anular(this);
    }

    public Leitor getSolicitante() {
        return solicitante;
    }

    public Exemplar getExemplar() {
        return item;
    }
}