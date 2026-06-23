package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Funcionario;
import br.edu.ufersa.model.services.FuncionarioService;
import br.edu.ufersa.view.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnEntrar;

    private final FuncionarioService funcionarioService = new FuncionarioService();

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

        Funcionario funcionario = funcionarioService.autenticar(usuario, senha);

        if (funcionario == null) {
            mostrarErro("Usuário ou senha incorretos.");
            txtSenha.clear();
            return;
        }

        MainApp.setFuncionarioLogado(funcionario);
        MainApp.telaDashboard();
        MainApp.getStage().setMaximized(true);
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}