package br.edu.ufersa.controller;

import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.model.entities.Pedido;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ModalNotaFiscalController {

    @FXML private TextArea txtNota;
    @FXML private Button btnFechar;

    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    public void setPedido(Pedido pedido) {
        txtNota.setText(sistema.gerarNotaPedido(pedido));
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
        stage.close();
    }
}