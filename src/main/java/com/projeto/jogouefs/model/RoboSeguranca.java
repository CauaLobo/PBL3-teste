package com.projeto.jogouefs.model;

public class RoboSeguranca extends Robo {

    public RoboSeguranca() {
        super(TipoRobo.SEGURANCA);
    }

    @Override
    public String trabalhar() {
        if (isQuebrado()) return "Robô Segurança está quebrado.";
        consumirEnergia(15); // Gasta mais energia patrulhando
        return "Robô Segurança patrulhou as ruas.";
    }
}
