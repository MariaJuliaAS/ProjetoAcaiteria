package br.edu.ufersa.controller;

import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.model.entities.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ClientesController {

    @FXML private FlowPane containerClientes;
    @FXML private TextField txtBusca;
    @FXML private Button btnBuscar;
    @FXML private Button btnNovoCliente;
    @FXML private SidebarController sidebarController;

    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    @FXML
    public void initialize() {
        carregarTodos();
        sidebarController.destacar(sidebarController.getBtnClientes());
    }

    private void carregarTodos() {
        List<Cliente> clientes = sistema.buscarClientesTodos();
        carregarCards(clientes);
    }

    private void carregarCards(List<Cliente> clientes) {
        containerClientes.getChildren().clear();

        for (Cliente cliente : clientes) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fxml/card-cliente.fxml"));

                Parent card = loader.load();

                CardClienteController controller =
                        loader.getController();

                controller.setDados(cliente);

                controller.setOnAlterado(this::carregarTodos);

                containerClientes.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void buscarCliente() {
        String termo = txtBusca.getText();

        if (termo == null || termo.trim().isEmpty()) {
            carregarTodos();
            return;
        }

        List<Cliente> resultado =
                sistema.buscarClientesPorNome(termo.trim());

        carregarCards(resultado);
    }

    @FXML
    private void limparBusca() {
        txtBusca.clear();
        carregarTodos();
    }

    @FXML
    private void abrirNovoCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal-novo-cliente.fxml"));
            Parent root = loader.load();

            ModalNovoClienteController controller = loader.getController();
            controller.setOnSalvar(this::carregarTodos);

            Stage modalStage = new Stage();
            modalStage.setTitle("Novo Cliente");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}