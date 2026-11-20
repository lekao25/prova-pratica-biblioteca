package siga;

import java.util.ArrayList;
import java.util.List;
/**
 * PROBLEMA 1 (parte A) - a conversao do formato legado feita aqui dentro,
 * e a dependencia da CLASSE CONCRETA do acervo central.
 *
 * Esta classe conhece o formato da matriz: que a coluna 0 e o codigo, a 1 e
 * a descricao e a 2 e a situacao em letra. Esse conhecimento deveria existir
 * em um unico lugar do sistema - e nao existe (ver RelatorioColecao).
 */

public class CatalogoExemplares {

    private final FonteExemplares fonte;

    public CatalogoExemplares(FonteExemplares fonte) {
        this.fonte = fonte;
    }

    public List<Exemplar> listarDisponiveis() {
        List<Exemplar> disponiveis = new ArrayList<>();
        for (Exemplar item : fonte.listarExemplares()) {
            if (item.isDisponivel()) {
                disponiveis.add(item);
            }
        }
        return disponiveis;
    }
}