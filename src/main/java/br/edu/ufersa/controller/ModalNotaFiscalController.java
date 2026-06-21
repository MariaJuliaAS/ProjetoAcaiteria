package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Pedido;
import br.edu.ufersa.model.services.PedidoService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ModalNotaFiscalController {

    @FXML private TextArea txtNota;
    @FXML private Button btnFechar;

    private final PedidoService service = new PedidoService();

    public void setPedido(Pedido pedido) {
        txtNota.setText(service.gerarNota(pedido));
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
        stage.close();
    }
}