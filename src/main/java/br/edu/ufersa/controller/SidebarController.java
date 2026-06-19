package br.edu.ufersa.controller;

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

    @FXML
    public void initialize() {
        // Texto provisório, até existir uma entidade de usuário/sessão.
        lblNomeUsuario.setText("Ju Maromba");
        lblCargoUsuario.setText("Admin");
    }

    // Os métodos abaixo ainda não fazem nada porque as telas
    // correspondentes (Dashboard, Pedidos, etc.) ainda não existem.
    // Conforme cada tela for criada, basta chamar aqui o método
    // estático equivalente em MainApp (ex: MainApp.telaDashboard()).

    @FXML
    private void sair() {
        // Quando a tela de login existir, troque para:
        // MainApp.telaDeLogin();
    }
}
