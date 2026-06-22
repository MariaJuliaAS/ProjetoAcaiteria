package br.edu.ufersa.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primeiroStage) {
        stage = primeiroStage;
        stage.setTitle("Sistema de Açaí");

        telaDashboard();

        stage.show();
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

    private static void trocarTela(String caminhoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(caminhoFxml));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}