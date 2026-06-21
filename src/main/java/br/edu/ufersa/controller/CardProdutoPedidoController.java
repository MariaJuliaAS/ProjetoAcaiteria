package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Produto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CardProdutoPedidoController {

    @FXML
    private Label lblProduto;
    @FXML private Label lblPreco;

    private Produto produto;
    private Runnable onAdicionar;

    public void setDados(Produto produto) {
        this.produto = produto;
        lblProduto.setText(produto.getNome());
        lblPreco.setText(String.format("R$ %.2f", produto.getPreco()));
    }

    public void setOnAdicionar(Runnable callback) {
        this.onAdicionar = callback;
    }

    @FXML
    private void adicionar() {
        if (onAdicionar != null) onAdicionar.run();
    }
}
