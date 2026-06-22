package br.edu.ufersa.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage stage;
    private static String tipoUsuario = "admin";
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

    public static void telaLogin() {
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

    public static String getTipoUsuario() {
        return tipoUsuario;
    }

    public static void setTipoUsuario(String tipo) {
        tipoUsuario = tipo;
    }

    public static void main(String[] args) {
        launch(args);
    }
}