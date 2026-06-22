package br.edu.ufersa.controller;

import br.edu.ufersa.view.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SidebarController {

    @FXML private Label lblNomeUsuario;
    @FXML private Label lblCargoUsuario;

    @FXML private Button btnDashboard;
    @FXML private Button btnPedidos;
    @FXML private Button btnClientes;
    @FXML private Button btnProdutos;
    @FXML private Button btnAdicionais;
    @FXML private Button btnRelatorios;
    @FXML private Button btnFuncionarios;
    @FXML private Button btnSair;

    private static final String ESTILO_NORMAL =
            "-fx-background-color: transparent; -fx-text-fill: #e0d0e8; -fx-font-size: 13px;";
    private static final String ESTILO_ATIVO =
            "-fx-background-color: #6b2d7a; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 8px;";

    @FXML
    public void initialize() {
        lblNomeUsuario.setText("Ju Maromba");
        lblCargoUsuario.setText("Admin");
    }

    public void destacar(Button botaoAtivo) {
        for (Button botao : new Button[]{btnDashboard, btnPedidos, btnClientes,
                btnProdutos, btnAdicionais, btnRelatorios, btnFuncionarios}) {
            botao.setStyle(botao == botaoAtivo ? ESTILO_ATIVO : ESTILO_NORMAL);
        }
    }

    public Button getBtnProdutos() {
        return btnProdutos;
    }

    public Button getBtnAdicionais() {
        return btnAdicionais;
    }

    public Button getBtnClientes() {
        return btnClientes;
    }

    public Button getBtnPedidos() {
        return btnPedidos;
    }

    public Button getBtnRelatorios() {
        return btnRelatorios;
    }

    public Button getBtnDashboard() {
        return btnDashboard;
    }

    @FXML
    private void irParaProdutos() {
        MainApp.telaProdutos();
    }

    @FXML
    private void irParaAdicionais() {
        MainApp.telaAdicionais();
    }

    @FXML
    private void irParaClientes() {
        MainApp.telaClientes();
    }

    @FXML
    private void irParaPedidos() {
        MainApp.telaPedidos();
    }

    @FXML
    private void irParaRelatorios() {
        MainApp.telaRelatorios();
    }

    @FXML
    private void irParaDashboard() {
        MainApp.telaDashboard();
    }

    @FXML
    private void sair(){}

}
