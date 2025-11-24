package com.projeto.jogouefs.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe base abstrata que representa um robô na cidade.
 * Implementa Serializable para permitir o salvamento do jogo.
 */
public abstract class Robo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private TipoRobo tipo;
    private int energia;
    private int felicidade;
    private boolean quebrado;

    /**
     * Construtor base para os robôs.
     * @param tipo O tipo do robô (Trabalhador, Engenheiro, Segurança).
     */
    public Robo(TipoRobo tipo) {
        this.id = UUID.randomUUID().toString(); // Gera ID único
        this.tipo = tipo;
        this.energia = 100; // Começa com bateria cheia
        this.felicidade = 100; // Começa feliz
        this.quebrado = false;
    }

    /**
     * Define a ação específica que cada robô realiza no turno.
     * @return Uma descrição do que foi feito.
     */
    public abstract String trabalhar();

    /**
     * Consome energia do robô. Se chegar a 0, o robô quebra.
     * @param quantidade Quantidade de energia a ser gasta.
     */
    public void consumirEnergia(int quantidade) {
        this.energia -= quantidade;
        if (this.energia <= 0) {
            this.energia = 0;
            this.quebrado = true;
        }
    }

    // Getters e Setters
    public String getId() { return id; }
    public TipoRobo getTipo() { return tipo; }

    public int getEnergia() { return energia; }
    public void setEnergia(int energia) {
        this.energia = Math.min(energia, 100); // Máximo 100
        if(this.energia > 0) this.quebrado = false;
    }

    public int getFelicidade() { return felicidade; }
    public void setFelicidade(int felicidade) {
        this.felicidade = Math.max(0, Math.min(felicidade, 100)); // Entre 0 e 100
    }

    public boolean isQuebrado() { return quebrado; }
    public void setQuebrado(boolean quebrado) { this.quebrado = quebrado; }
}