package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Adicional;
import br.edu.ufersa.model.services.AdicionalService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CardAdicionalController {

    @FXML private FlowPane containerAdicionais;
    @FXML private VBox cardRoot;
    @FXML private Label lblNome;
    @FXML private Label lblPreco;
    @FXML private Label lblEstoque;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private Adicional adicional;
    private final AdicionalService service = new AdicionalService();

    private Runnable onAlterado;

    public void setDados(Adicional adicional) {
        this.adicional = adicional;
        lblNome.setText(adicional.getNome());
        lblPreco.setText(String.format("R$ %.2f", adicional.getPreco()));
        lblEstoque.setText("Estoque: " + adicional.getQtdEstoque() + " un.");
    }

    public void setOnAlterado(Runnable onAlterado) {
        this.onAlterado = onAlterado;
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
    private void excluir() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja excluir o adicional \"" + adicional.getNome() + "\"?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta.getText().equalsIgnoreCase("OK")) {
                try {
                    service.excluirAdicional(adicional);
                    if (onAlterado != null) {
                        onAlterado.run();
                    }
                } catch (IllegalArgumentException e) {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro ao excluir");
                    erro.setContentText(e.getMessage());
                    erro.showAndWait();
                }
            }
        });
    }

    @FXML
    private void editar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal-editar-adicional.fxml"));
            Parent root = loader.load();

            ModalNovoAdicionalController controller = loader.getController();
            controller.setOnSalvar(this::carregarTodos);

            Stage modalStage = new Stage();
            modalStage.setTitle("Editar Adicional");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
