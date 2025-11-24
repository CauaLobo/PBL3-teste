package com.projeto.jogouefs.controllers;

import com..jogouefs.model.*;
import com.projeto.jogouefs.model.*;

import java.io.IOException;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador principal que gerencia a lógica de negócio do jogo.
 */
public class GameController {

    private Cidade cidade;
    private ArquivoController arquivoController;
    private Random random;

    // Log de eventos para mostrar na interface (ex: "Dia 5: Um apagão ocorreu!")
    private List<String> logEventos;

    public GameController() {
        this.arquivoController = new ArquivoController();
        this.random = new Random();
        this.logEventos = new ArrayList<>();
    }

    /**
     * Inicia um novo jogo criando uma nova cidade.
     */
    public void criarNovaCidade(String nome) {
        this.cidade = new Cidade(nome);
        this.logEventos.clear();
        this.logEventos.add("Cidade " + nome + " fundada com sucesso!");
    }

    /**
     * Avança para o próximo turno (Dia).
     * 1. Consome energia/manutenção.
     * 2. Robôs trabalham.
     * 3. Sorteia eventos aleatórios.
     */
    public void passarTurno() {
        if (cidade == null) return;

        cidade.avancarDia();
        logEventos.add("--- Dia " + cidade.getDiaAtual() + " iniciado ---");

        // 1. Processar Robôs
        processarRobos();

        // 2. Processar Prédios (Manutenção)
        processarPredios();

        // 3. Eventos Aleatórios (20% de chance de ocorrer algo)
        if (random.nextInt(100) < 20) {
            processarEventosAleatorios();
        }

        // Atualiza status final
        logEventos.add("Felicidade Média: " + String.format("%.2f", cidade.calcularFelicidadeMedia()));
    }

    private void processarRobos() {
        for (Robo robo : cidade.getRobos()) {
            if (robo.isQuebrado()) {
                logEventos.add("ALERTA: Robô " + robo.getTipo() + " está quebrado e inativo.");
                continue;
            }

            // O robô trabalha e gera retorno (string)
            String resultadoTrabalho = robo.trabalhar();

            // Lógica específica de recompensa baseada no tipo (Regra de Negócio)
            if (robo instanceof RoboTrabalhador) {
                cidade.setRecursos(cidade.getRecursos() + 50); // Gera dinheiro
            }
            // Engenheiros e Seguranças têm efeitos passivos ou de manutenção implícita

            // Consumo de felicidade natural pelo trabalho
            int perdaFelicidade = (robo instanceof RoboSeguranca) ? 2 : 5;
            robo.setFelicidade(robo.getFelicidade() - perdaFelicidade);
        }
    }

    private void processarPredios() {
        for (Predio predio : cidade.getPredios()) {
            // Desconta custo de manutenção
            cidade.setRecursos(cidade.getRecursos() - predio.getCustoManutencao());

            // Se for usina, recarrega robôs (simplificação)
            if (predio.getGeracaoEnergia() > 0) {
                for (Robo r : cidade.getRobos()) {
                    if (!r.isQuebrado()) {
                        r.setEnergia(r.getEnergia() + 10);
                    }
                }
            }
        }
    }

    /**
     * Implementa os 3 eventos aleatórios exigidos: Apagão, Greve, Peças Raras.
     */
    private void processarEventosAleatorios() {
        int tipoEvento = random.nextInt(3); // 0, 1 ou 2

        switch (tipoEvento) {
            case 0: // Apagão
                logEventos.add("EVENTO: Um Grande APAGÃO atingiu a cidade! Todos os robôs perderam energia.");
                for (Robo r : cidade.getRobos()) r.consumirEnergia(30);
                break;

            case 1: // Greve
                logEventos.add("EVENTO: GREVE dos robôs! A felicidade caiu drasticamente.");
                for (Robo r : cidade.getRobos()) r.setFelicidade(r.getFelicidade() - 20);
                break;

            case 2: // Peças Raras
                logEventos.add("EVENTO: Descoberta de PEÇAS RARAS! Recursos aumentaram.");
                cidade.setRecursos(cidade.getRecursos() + 500);
                break;
        }
    }

    // --- Métodos de Ação do Usuário (Fachada) ---

    public void contratarRobo(TipoRobo tipo) {
        double custo = 100.0;
        if (cidade.getRecursos() >= custo) {
            cidade.setRecursos(cidade.getRecursos() - custo);
            Robo novo = null;
            switch(tipo) {
                case TRABALHADOR: novo = new RoboTrabalhador(); break;
                case ENGENHEIRO: novo = new RoboEngenheiro(); break;
                case SEGURANCA: novo = new RoboSeguranca(); break;
            }
            cidade.adicionarRobo(novo);
            logEventos.add("Novo robô " + tipo + " contratado.");
        } else {
            logEventos.add("Erro: Recursos insuficientes para contratar.");
        }
    }

    public void construirPredio(String tipo, int custo, int energia) {
        if (cidade.getRecursos() >= custo) {
            cidade.setRecursos(cidade.getRecursos() - custo);
            Predio p = new Predio(tipo, 10, energia); // 10 de manutenção fixa por enquanto
            cidade.adicionarPredio(p);
            logEventos.add("Prédio " + tipo + " construído.");
        } else {
            logEventos.add("Erro: Recursos insuficientes para construir.");
        }
    }

    // --- Métodos de Persistência ---

    public void salvarCidade() throws IOException {
        if (cidade != null) {
            arquivoController.salvarJogo(cidade, cidade.getNome());
            logEventos.add("Jogo salvo com sucesso!");
        }
    }

    public void carregarCidade(String nome) throws IOException, ClassNotFoundException {
        this.cidade = arquivoController.carregarJogo(nome);
        logEventos.add("Jogo carregado: " + nome);
    }

    // Getters para a View usar
    public Cidade getCidade() { return cidade; }
    public List<String> getLogEventos() { return logEventos; }
}
