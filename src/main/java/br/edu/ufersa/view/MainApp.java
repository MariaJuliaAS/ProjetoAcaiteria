package br.edu.ufersa.view;

import br.edu.ufersa.model.entities.Funcionario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage stage;
    private static Funcionario funcionarioLogado;
    private static Scene scene;

    @Override
    public void start(Stage primeiroStage) {
        stage = primeiroStage;
        stage.setTitle("Sistema de Açaí");

        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("/fxml/login.fxml"));

            scene = new Scene(root);

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void telaDashboard() {
        trocarTela("/fxml/dashboard.fxml");
    }

    public static void telaAdicionais() {
        trocarTela("/fxml/adicionais.fxml");
    }

    public static void telaProdutos() {
        trocarTela("/fxml/produtos.fxml");
    }

    public static void telaPedidos() {
        trocarTela("/fxml/pedidos.fxml");
    }

    public static void telaClientes() {
        trocarTela("/fxml/clientes.fxml");
    }

    public static void telaRelatorios() {
        trocarTela("/fxml/relatorios.fxml");
    }

    public static void telaFuncionarios() {
        trocarTela("/fxml/funcionarios.fxml");
    }

    public static void telaLogin() {
        funcionarioLogado = null;
        trocarTela("/fxml/login.fxml");
    }

    private static void trocarTela(String caminhoFxml) {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource(caminhoFxml));

            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getStage() {
        return stage;
    }

    public static Funcionario getFuncionarioLogado() {
        return funcionarioLogado;
    }

    public static void setFuncionarioLogado(Funcionario funcionario) {
        funcionarioLogado = funcionario;
    }

    public static boolean isAdminLogado() {
        return funcionarioLogado != null && "Admin".equalsIgnoreCase(funcionarioLogado.getTipo());
    }

    public static void main(String[] args) {
        launch(args);
    }
}