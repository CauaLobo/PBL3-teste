package com.projeto.jogouefs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a cidade e todo o estado atual do jogo.
 * Funciona como o objeto raiz para persistência.
 */
public class idade implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private int diaAtual;
    private double recursos; // Dinheiro/Peças
    private List<Robo> robos;
    private List<Predio> predios;

    public Cidade(String nome) {
        this.nome = nome;
        this.diaAtual = 1;
        this.recursos = 1000.0; // Recursos iniciais
        this.robos = new ArrayList<>();
        this.predios = new ArrayList<>();
    }

    // Métodos utilitários para facilitar o Controller depois

    public void adicionarRobo(Robo robo) {
        this.robos.add(robo);
    }

    public void adicionarPredio(Predio predio) {
        this.predios.add(predio);
    }

    public void avancarDia() {
        this.diaAtual++;
    }

    public double calcularFelicidadeMedia() {
        if (robos.isEmpty()) return 0.0;
        double total = 0;
        for (Robo r : robos) {
            total += r.getFelicidade();
        }
        return total / robos.size();
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public int getDiaAtual() { return diaAtual; }
    public double getRecursos() { return recursos; }
    public void setRecursos(double recursos) { this.recursos = recursos; }
    public List<Robo> getRobos() { return robos; }
    public List<Predio> getPredios() { return predios; }
}
