package com.projeto.jogouefs.view;

import com.projeto.jogouefs.controllers.GameController;
import com.projeto.jogouefs.model.Predio;
import com.projeto.jogouefs.model.Robo;
import com.projeto.jogouefs.model.TipoRobo;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;

public class MainApp extends Application {

    private GameController controller;

    // Componentes da Interface que precisam ser atualizados
    private Label lblDia;
    private Label lblRecursos;
    private Label lblFelicidade;
    private FlowPane painelCidade; // Onde desenharemos os robôs e prédios
    private TextArea areaLog; // Console de mensagens

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        controller = new GameController();

        // Pergunta o nome da cidade ao iniciar
        TextInputDialog dialog = new TextInputDialog("RoboCity");
        dialog.setTitle("Nova Cidade");
        dialog.setHeaderText("Bem-vindo, Gestor!");
        dialog.setContentText("Dê um nome para sua cidade:");
        Optional<String> result = dialog.showAndWait();
        String nomeCidade = result.orElse("Cidade Padrão");

        controller.criarNovaCidade(nomeCidade);

        // --- Layout Principal (BorderPane) ---
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // 1. TOPO: Barra de Status
        HBox topBar = criarBarraStatus();
        root.setTop(topBar);

        // 2. CENTRO: A Cidade (Visualização)
        ScrollPane scrollPane = new ScrollPane();
        painelCidade = new FlowPane();
        painelCidade.setPadding(new Insets(10));
        painelCidade.setHgap(10);
        painelCidade.setVgap(10);
        painelCidade.setStyle("-fx-background-color: #f0f0f0;");
        scrollPane.setContent(painelCidade);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        // 3. DIREITA: Painel de Controle e Logs
        VBox rightPanel = criarPainelControle();
        root.setRight(rightPanel);

        // Configuração da Cena
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Cidade dos Robôs - Gestão: " + nomeCidade);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Primeira atualização da tela
        atualizarInterface();
    }

    private HBox criarBarraStatus() {
        HBox hbox = new HBox(20);
        hbox.setPadding(new Insets(10));
        hbox.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        hbox.setAlignment(Pos.CENTER_LEFT);

        lblDia = new Label("Dia: 1");
        lblRecursos = new Label("Recursos: 1000");
        lblFelicidade = new Label("Felicidade: 100%");

        // Estilizando labels
        String estilo = "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;";
        lblDia.setStyle(estilo);
        lblRecursos.setStyle(estilo);
        lblFelicidade.setStyle(estilo);

        hbox.getChildren().addAll(lblDia, lblRecursos, lblFelicidade);
        return hbox;
    }

    private VBox criarPainelControle() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setPrefWidth(250);
        vbox.setStyle("-fx-background-color: #ddd; -fx-border-color: #999;");

        Label lblAcoes = new Label("Painel de Controle");
        lblAcoes.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Botão Próximo Turno
        Button btnTurno = new Button(">>> AVANÇAR DIA <<<");
        btnTurno.setMaxWidth(Double.MAX_VALUE);
        btnTurno.setStyle("-fx-background-color: #ff9900; -fx-text-fill: white; -fx-font-weight: bold;");
        btnTurno.setOnAction(e -> {
            controller.passarTurno();
            atualizarInterface();
        });

        Separator sep1 = new Separator();

        // Botões de Contratação
        Label lblContratar = new Label("Contratar Robôs ($100)");
        Button btnTrab = new Button("Trabalhador (+Recurso)");
        Button btnEng = new Button("Engenheiro (Manutenção)");
        Button btnSeg = new Button("Segurança (Proteção)");

        configurarBotaoLargo(btnTrab, btnEng, btnSeg);

        btnTrab.setOnAction(e -> { controller.contratarRobo(TipoRobo.TRABALHADOR); atualizarInterface(); });
        btnEng.setOnAction(e -> { controller.contratarRobo(TipoRobo.ENGENHEIRO); atualizarInterface(); });
        btnSeg.setOnAction(e -> { controller.contratarRobo(TipoRobo.SEGURANCA); atualizarInterface(); });

        Separator sep2 = new Separator();

        // Botões de Construção
        Label lblConstruir = new Label("Construir");
        Button btnUsina = new Button("Usina Energia ($300)");
        Button btnFabrica = new Button("Fábrica Peças ($200)");

        configurarBotaoLargo(btnUsina, btnFabrica);

        btnUsina.setOnAction(e -> { controller.construirPredio("Usina", 300, 50); atualizarInterface(); });
        btnFabrica.setOnAction(e -> { controller.construirPredio("Fábrica", 200, 0); atualizarInterface(); });

        Separator sep3 = new Separator();

        // Persistência
        Button btnSalvar = new Button("Salvar Jogo");
        Button btnCarregar = new Button("Carregar Jogo");
        configurarBotaoLargo(btnSalvar, btnCarregar);

        btnSalvar.setOnAction(e -> {
            try {
                controller.salvarCidade();
                mostrarAlerta("Sucesso", "Jogo salvo com sucesso!");
                atualizarInterface();
            } catch (Exception ex) {
                mostrarAlerta("Erro", "Erro ao salvar: " + ex.getMessage());
            }
        });

        btnCarregar.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Carregar");
            dialog.setContentText("Nome da cidade para carregar:");
            dialog.showAndWait().ifPresent(nome -> {
                try {
                    controller.carregarCidade(nome);
                    atualizarInterface();
                } catch (Exception ex) {
                    mostrarAlerta("Erro", "Erro ao carregar: " + ex.getMessage());
                }
            });
        });

        // Área de Log (Console)
        areaLog = new TextArea();
        areaLog.setEditable(false);
        areaLog.setWrapText(true);
        areaLog.setPrefHeight(200);

        vbox.getChildren().addAll(
                lblAcoes, btnTurno, sep1,
                lblContratar, btnTrab, btnEng, btnSeg, sep2,
                lblConstruir, btnUsina, btnFabrica, sep3,
                btnSalvar, btnCarregar, new Label("Eventos:"), areaLog
        );

        return vbox;
    }

    private void configurarBotaoLargo(Button... buttons) {
        for (Button b : buttons) {
            b.setMaxWidth(Double.MAX_VALUE);
        }
    }

    /**
     * O "Coração" da View.
     * Este método pega o estado atual do Model e redesenha a tela.
     */
    private void atualizarInterface() {
        if (controller.getCidade() == null) return;

        // 1. Atualizar Textos
        lblDia.setText("Dia: " + controller.getCidade().getDiaAtual());
        lblRecursos.setText(String.format("Recursos: $%.1f", controller.getCidade().getRecursos()));
        lblFelicidade.setText(String.format("Felicidade: %.1f%%", controller.getCidade().calcularFelicidadeMedia()));

        // 2. Atualizar Logs
        StringBuilder logs = new StringBuilder();
        // Pegamos os últimos eventos (inverso para mostrar o mais recente embaixo)
        for (String msg : controller.getLogEventos()) {
            logs.append(msg).append("\n");
        }
        areaLog.setText(logs.toString());
        areaLog.setScrollTop(Double.MAX_VALUE); // Auto-scroll para o final

        // 3. Redesenhar a Cidade (Limpa e desenha de novo)
        painelCidade.getChildren().clear();

        // Desenhar Prédios
        for (Predio p : controller.getCidade().getPredios()) {
            painelCidade.getChildren().add(criarCardPredio(p));
        }

        // Desenhar Robôs
        for (Robo r : controller.getCidade().getRobos()) {
            painelCidade.getChildren().add(criarCardRobo(r));
        }
    }

    // --- Componentes Visuais (Cards) ---

    private VBox criarCardRobo(Robo r) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(100, 120);

        // Cor baseada no tipo
        String corFundo = "#CCCCCC";
        String icone = "🤖";

        switch (r.getTipo()) {
            case TRABALHADOR: corFundo = "#ADD8E6"; icone = "🔨"; break; // Azul
            case ENGENHEIRO: corFundo = "#90EE90"; icone = "🔧"; break; // Verde
            case SEGURANCA: corFundo = "#FFB6C1"; icone = "🛡️"; break; // Vermelho
        }

        if (r.isQuebrado()) {
            corFundo = "#555555"; // Cinza escuro se quebrado
            icone = "☠️";
        }

        card.setStyle("-fx-background-color: " + corFundo + "; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);");

        Label lblIcone = new Label(icone);
        lblIcone.setFont(Font.font(30));

        Label lblTipo = new Label(r.getTipo().toString().substring(0, 4));
        Label lblEnergia = new Label("E: " + r.getEnergia());
        Label lblHappy = new Label("F: " + r.getFelicidade());

        card.getChildren().addAll(lblIcone, lblTipo, lblEnergia, lblHappy);
        return card;
    }

    private VBox criarCardPredio(Predio p) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(120, 120);

        String cor = p.getTipo().equals("Usina") ? "#FFA500" : "#8A2BE2"; // Laranja ou Roxo

        card.setStyle("-fx-background-color: white; -fx-border-color: " + cor + "; -fx-border-width: 3; -fx-background-radius: 5;");

        Label lblIcone = new Label("🏢");
        lblIcone.setFont(Font.font(30));
        Label lblNome = new Label(p.getTipo());

        card.getChildren().addAll(lblIcone, lblNome);
        return card;
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}