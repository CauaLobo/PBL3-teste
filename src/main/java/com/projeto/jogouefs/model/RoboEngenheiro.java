package com.projeto.jogouefs.model;

public class RoboEngenheiro extends Robo {

    public RoboEngenheiro() {
        super(TipoRobo.ENGENHEIRO);
    }

    @Override
    public String trabalhar() {
        if (isQuebrado()) return "Robô Engenheiro está quebrado.";
        consumirEnergia(5); // Gasta menos energia por ser eficiente
        return "Robô Engenheiro realizou manutenções preventivas.";
    }
}
