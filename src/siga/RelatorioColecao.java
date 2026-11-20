package siga;

import java.util.List;
/**
 * PROBLEMA 1 (parte B) - a MESMA conversao, escrita de novo, em outro lugar.
 *
 * Aqui alguem percebeu que a situacao as vezes vem em minuscula e corrigiu.
 * A correcao nao chegou ao CatalogoExemplares: as duas versoes ja divergem, e o
 * mesmo Exemplar aparece indisponivel em um lugar e disponivel no outro.
 */

public class RelatorioColecao {

    private final FonteExemplares fonte;

    public RelatorioColecao(FonteExemplares fonte) {
        this.fonte = fonte;
    }

    public List<Exemplar> listarTodos() {
        return fonte.listarExemplares();
    }

    public void imprimir() {
        System.out.println("   Situacao segundo o relatorio:");
        for (Exemplar item : listarTodos()) {
            System.out.println("     " + item + " - "
                    + (item.isDisponivel() ? "disponivel" : "baixado"));
        }
    }
}