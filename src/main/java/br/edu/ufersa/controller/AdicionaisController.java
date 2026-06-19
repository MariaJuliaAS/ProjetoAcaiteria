package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Adicional;
import br.edu.ufersa.model.services.AdicionalService;
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

public class AdicionaisController {

    @FXML private FlowPane containerAdicionais;
    @FXML private TextField txtBusca;
    @FXML private Button btnBuscar;
    @FXML private Button btnNovoAdicional;

    private final AdicionalService service = new AdicionalService();

    @FXML
    public void initialize() {
        carregarTodos();
    }

    private void carregarTodos() {
        List<Adicional> adicionais = service.buscarTodos();
        carregarCards(adicionais);
    }

    private void carregarCards(List<Adicional> adicionais) {
        containerAdicionais.getChildren().clear();

        for (Adicional adicional : adicionais) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/card-adicional.fxml"));
                Parent card = loader.load();

                CardAdicionalController controller = loader.getController();
                controller.setDados(adicional);

                controller.setOnAlterado(this::carregarTodos);

                containerAdicionais.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void buscarAdicional() {
        String termo = txtBusca.getText();

        if (termo == null || termo.trim().isEmpty()) {
            carregarTodos();
            return;
        }

        List<Adicional> resultado = service.buscarPorNome(termo.trim());
        carregarCards(resultado);
    }

    @FXML
    private void limparBusca() {
        txtBusca.clear();
        carregarTodos();
    }

    @FXML
    private void abrirNovoAdicional() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal-novo-adicional.fxml"));
            Parent root = loader.load();

            ModalNovoAdicionalController controller = loader.getController();
            controller.setOnSalvar(this::carregarTodos);

            Stage modalStage = new Stage();
            modalStage.setTitle("Novo Adicional");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
