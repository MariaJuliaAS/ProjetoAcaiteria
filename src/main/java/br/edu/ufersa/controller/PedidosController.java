package br.edu.ufersa.controller;

import br.edu.ufersa.facade.SistemaAcaiteria;
import br.edu.ufersa.model.entities.Cliente;
import br.edu.ufersa.model.entities.Pedido;
import br.edu.ufersa.model.entities.Produto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PedidosController {

    @FXML private FlowPane containerPedidos;
    @FXML private TextField txtBusca;
    @FXML private Button btnBuscarData;
    @FXML private Button btnBuscarCliente;
    @FXML private Button btnBuscarProduto;
    @FXML private Button btnNovoPedido;
    @FXML private ComboBox<Produto> cmbProduto;
    @FXML private SidebarController sidebarController;

    private final SistemaAcaiteria sistema = new SistemaAcaiteria();

    @FXML
    public void initialize() {
        carregarTodos();
        carregarProdutosComboBox();
        sidebarController.destacar(sidebarController.getBtnPedidos());
    }

    private void carregarTodos() {
        List<Pedido> pedidos = sistema.buscarPedidosTodos();
        carregarCards(pedidos);
    }

    private void carregarCards(List<Pedido> pedidos) {
        containerPedidos.getChildren().clear();

        for (Pedido pedido : pedidos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/card-pedido.fxml"));
                Parent card = loader.load();

                CardPedidoController controller = loader.getController();
                controller.setDados(pedido);
                controller.setOnAlterado(this::carregarTodos);

                containerPedidos.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void carregarProdutosComboBox() {
        List<Produto> produtos = sistema.buscarProdutoTodos();
        cmbProduto.getItems().addAll(produtos);

        cmbProduto.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Produto p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNome());
            }
        });
        cmbProduto.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Produto p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNome());
            }
        });
    }

    @FXML
    private void buscarPedidoPorData() {
        String termo = txtBusca.getText().trim();

        if (termo.isEmpty()) {
            carregarTodos();
            return;
        }

        try {
            LocalDate data = LocalDate.parse(termo,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            List<Pedido> resultado = sistema.buscarPedidoPorData(data);
            carregarCards(resultado);
        } catch (DateTimeParseException e) {
            mostrarErro("Digite a data no formato dd/MM/yyyy.");
        }
    }

    @FXML
    private void buscarPedidoPorCliente() {
        String termo = txtBusca.getText().trim();

        if (termo.isEmpty()) {
            carregarTodos();
            return;
        }

        List<Cliente> clientes = sistema.buscarClientesPorNome(termo);

        if (clientes.isEmpty()) {
            mostrarErro("Nenhum cliente encontrado com esse nome.");
            return;
        }

        List<Pedido> resultado = new ArrayList<>();
        for (Cliente cliente : clientes) {
            resultado.addAll(sistema.buscarPedidoPorCliente(cliente));
        }

        carregarCards(resultado);
    }

    @FXML
    private void buscarPedidoPorProduto() {
        Produto produto = cmbProduto.getValue();

        if (produto == null) {
            mostrarErro("Selecione um produto.");
            return;
        }

        List<Pedido> resultado = sistema.buscarPedidoPorProduto(produto);
        carregarCards(resultado);
    }

    @FXML
    private void limparBusca() {
        txtBusca.clear();
        cmbProduto.setValue(null);
        carregarTodos();
    }

    @FXML
    private void abrirNovoPedido() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/modal-novo-pedido.fxml"));
            Parent root = loader.load();

            ModalNovoPedidoController controller = loader.getController();
            controller.setOnSalvar(this::carregarTodos);

            Stage modalStage = new Stage();
            modalStage.setTitle("Novo Pedido");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setMaximized(true);
            modalStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}