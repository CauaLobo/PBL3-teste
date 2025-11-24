package com.projeto.jogouefs.model;

import java.io.Serializable;

/**
 * Representa uma construção na cidade.
 */
public class Predio implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tipo; // Ex: "Usina", "Fabrica", "Residencia"
    private int custoManutencao;
    private int geracaoEnergia; // Se > 0, é uma usina

    public Predio(String tipo, int custoManutencao, int geracaoEnergia) {
        this.tipo = tipo;
        this.custoManutencao = custoManutencao;
        this.geracaoEnergia = geracaoEnergia;
    }

    public String getTipo() { return tipo; }
    public int getCustoManutencao() { return custoManutencao; }
    public int getGeracaoEnergia() { return geracaoEnergia; }
}
