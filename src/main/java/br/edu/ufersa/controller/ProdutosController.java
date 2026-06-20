package br.edu.ufersa.controller;

import br.edu.ufersa.model.entities.Produto;
import br.edu.ufersa.model.services.ProdutoService;
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

public class ProdutosController {

    @FXML private FlowPane containerProdutos;
    @FXML private TextField txtBusca;
    @FXML private Button btnBuscar;
    @FXML private Button btnNovoProduto;

    @FXML private SidebarController sidebarController;

    private final ProdutoService service = new ProdutoService();

    @FXML
    public void initialize() {
        carregarTodos();
        sidebarController.destacar(sidebarController.getBtnProdutos());
    }

    private void carregarTodos() {
        List<Produto> produtos = service.buscarTodos();
        carregarCards(produtos);
    }

    private void carregarCards(List<Produto> produtos) {
        containerProdutos.getChildren().clear();

        for (Produto produto : produtos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/card-produto.fxml"));
                Parent card = loader.load();

                CardProdutoController controller = loader.getController();
                controller.setDados(produto);
                controller.setOnAlterado(this::carregarTodos);

                containerProdutos.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void buscarProduto() {
        String termo = txtBusca.getText();

        if (termo == null || termo.trim().isEmpty()) {
            carregarTodos();
            return;
        }

        List<Produto> todos = service.buscarTodos();
        List<Produto> filtrados = todos.stream()
                .filter(p -> p.getNome().toLowerCase().contains(termo.trim().toLowerCase()))
                .toList();

        carregarCards(filtrados);
    }

    @FXML
    private void limparBusca() {
        txtBusca.clear();
        carregarTodos();
    }

    @FXML
    private void abrirNovoProduto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modal-produto.fxml"));
            Parent root = loader.load();

            ModalProdutoController controller = loader.getController();
            controller.setOnSalvar(this::carregarTodos);

            Stage modalStage = new Stage();
            modalStage.setTitle("Novo Produto");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
