package br.edu.ufersa.controller;

import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.model.entities.Funcionario;
import br.edu.ufersa.view.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FuncionariosController {

    @FXML private FlowPane containerFuncionarios;
    @FXML private Button btnNovoFuncionario;

    @FXML private SidebarController sidebarController;

    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    @FXML
    public void initialize() {

        if (!MainApp.isAdminLogado()) {
            MainApp.telaDashboard();
            return;
        }

        carregarTodos();
        sidebarController.destacar(sidebarController.getBtnFuncionarios());
    }

    private void carregarTodos() {
        List<Funcionario> funcionarios = sistema.buscarFuncionariosTodos();
        carregarCards(funcionarios);
    }

    private void carregarCards(List<Funcionario> funcionarios) {
        containerFuncionarios.getChildren().clear();

        for (Funcionario funcionario : funcionarios) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/card-funcionario.fxml"));
                Parent card = loader.load();

                CardFuncionarioController controller = loader.getController();
                controller.setDados(funcionario);
                controller.setOnAlterado(this::carregarTodos);

                containerFuncionarios.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void abrirNovoFuncionario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal-funcionario.fxml"));
            Parent root = loader.load();

            ModalFuncionarioController controller = loader.getController();
            controller.setOnSalvar(this::carregarTodos);

            Stage modalStage = new Stage();
            modalStage.setTitle("Novo Funcionário");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
