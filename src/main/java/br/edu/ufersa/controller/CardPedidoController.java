package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.ItemPedido;
import br.edu.ufersa.model.entities.Pedido;
import br.edu.ufersa.model.services.PedidoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;


public class CardPedidoController {

    @FXML private Label lblCliente;
    @FXML private Label lblData;
    @FXML private Label lblQtdItem;
    @FXML private Label lblValor;
    @FXML private Label lblFormaPagamento;

    private Pedido pedido;
    private final PedidoService service = new PedidoService();

    private Runnable onAlterado;

    public void setDados(Pedido pedido) {
        this.pedido = pedido;
        lblCliente.setText(pedido.getCliente().getNome());
        lblData.setText(pedido.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        int qtdItem = pedido.getItensPedido()
                .stream()
                .mapToInt(ItemPedido::getQuantidade)
                .sum();
        lblQtdItem.setText(qtdItem + (qtdItem > 1 ? " Itens" : " Item"));
        lblFormaPagamento.setText(pedido.getFormaPagamento());
        lblValor.setText(String.format("R$ %.2f",service.calcularTotal(pedido)));
    }

    public void setOnAlterado(Runnable onAlterado) {
        this.onAlterado = onAlterado;
    }

    @FXML
    private void excluir() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja excluir o pedido com o ID \"" + pedido.getId() + "\"?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta.getText().equalsIgnoreCase("OK")) {
                try {
                    service.excluir(pedido);
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
    private void gerarNota() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal-nota-fiscal.fxml"));
            Parent root = loader.load();

            ModalNotaFiscalController controller = loader.getController();
            controller.setPedido(pedido);

            Stage modalStage = new Stage();
            modalStage.setTitle("Nota Fiscal");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

