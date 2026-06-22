package br.edu.ufersa.controller;

import br.edu.ufersa.view.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnEntrar;
    @FXML private Button btnAlternar;

    private boolean modoAdmin = true;

    @FXML
    public void initialize() {
        atualizarModo();
    }

    private void atualizarModo() {
        if (modoAdmin) {
            txtUsuario.setPromptText("Admin");
            btnAlternar.setText("Entrar como funcionário");
        } else {
            txtUsuario.setPromptText("Funcionário");
            btnAlternar.setText("Entrar como admin");
        }
        txtUsuario.clear();
        txtSenha.clear();
    }

    @FXML
    private void entrar() {
        String usuario = txtUsuario.getText().trim();
        String senha = txtSenha.getText();

        if (usuario.isEmpty()) {
            mostrarErro("Preencha usuário");
            return;
        }

        if (senha.isEmpty()) {
            mostrarErro("Preencha senha");
            return;
        }

        if (modoAdmin) {
            if (usuario.equals("admin") && senha.equals("admin")) {
                MainApp.setTipoUsuario("admin");
                MainApp.telaDashboard();
                MainApp.getStage().setMaximized(true);
            } else {
                mostrarErro("Usuário ou senha incorretos.");
                txtSenha.clear();
            }
        } else {
            if (usuario.equals("funcionario") && senha.equals("funcionario")) {
                MainApp.setTipoUsuario("funcionario");
                MainApp.telaDashboard();
                MainApp.getStage().setMaximized(true);
            } else {
                mostrarErro("Usuário ou senha incorretos.");
                txtSenha.clear();
            }
        }
    }

    @FXML
    private void alternar() {
        modoAdmin = !modoAdmin;
        atualizarModo();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}