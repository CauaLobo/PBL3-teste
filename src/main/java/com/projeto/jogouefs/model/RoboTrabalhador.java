package com.projeto.jogouefs.model;

public class RoboTrabalhador extends Robo {

    public RoboTrabalhador() {
        super(TipoRobo.TRABALHADOR);
    }

    @Override
    public String trabalhar() {
        if (isQuebrado()) return "Robô Trabalhador está quebrado e não pode trabalhar.";
        consumirEnergia(10);
        return "Robô Trabalhador gerou recursos para a cidade.";
    }
}